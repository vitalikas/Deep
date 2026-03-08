package lt.vitalijus.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Android implementation of TokenStorage using:
 * - DataStore for persistent storage
 * - Android Keystore for key management
 * - AES/GCM/NoPadding for encryption
 * - StateFlow for reactive token updates
 */
class AndroidTokenStorage(
    context: Context,
    private val logger: Logger
) : TokenStorage {

    private val dataStore: DataStore<Preferences> = context.dataStore
    private val dataStoreTokenKey = stringPreferencesKey(KEY_TOKEN)
    private val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)

    private val _tokenFlow = MutableStateFlow<String?>(null)
    override val tokenFlow: StateFlow<String?> = _tokenFlow

    companion object {
        private const val STORAGE_NAME = "secure_storage"
        private const val KEY_TOKEN = "auth_token"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEYSTORE_ALIAS = "deep_auth_token_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH = 128
        private const val GCM_IV_LENGTH = 12

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = STORAGE_NAME
        )
    }

    init {
        generateKeyIfNecessary()
    }

    private fun generateKeyIfNecessary() {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                val keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    ANDROID_KEYSTORE
                )

                keyGenerator.init(
                    KeyGenParameterSpec.Builder(
                        KEYSTORE_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(true)
                        .build()
                )

                keyGenerator.generateKey()
                logger.i { "Generated new encryption key in Keystore" }
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to generate encryption key" }
            throw SecureStorageException("Failed to initialize secure storage", e)
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEYSTORE_ALIAS, null) as? SecretKey
            ?: throw SecureStorageException("Encryption key not found in Keystore")
    }

    private fun encrypt(plaintext: String): String {
        return try {
            val secretKey = getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val iv = cipher.iv
            val encrypted = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

            val combined = ByteArray(iv.size + encrypted.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)

            Base64.encodeToString(combined, Base64.NO_WRAP)
        } catch (e: Exception) {
            logger.e(e) { "Encryption failed" }
            throw SecureStorageException("Failed to encrypt token", e)
        }
    }

    private fun decrypt(ciphertext: String): String {
        return try {
            val combined = Base64.decode(ciphertext, Base64.NO_WRAP)

            val iv = ByteArray(GCM_IV_LENGTH)
            val encrypted = ByteArray(combined.size - GCM_IV_LENGTH)
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH)
            System.arraycopy(combined, GCM_IV_LENGTH, encrypted, 0, encrypted.size)

            val secretKey = getSecretKey()
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
            String(cipher.doFinal(encrypted), Charsets.UTF_8)
        } catch (e: Exception) {
            logger.e(e) { "Decryption failed" }
            throw SecureStorageException("Failed to decrypt token", e)
        }
    }

    override suspend fun hasToken(): Boolean {
        return try {
            dataStore.data
                .map { preferences ->
                    preferences.contains(dataStoreTokenKey)
                }
                .first()
        } catch (e: Exception) {
            logger.e(e) { "Failed to check token existence" }
            false
        }
    }

    override suspend fun saveToken(token: String) {
        try {
            val encrypted = encrypt(token)
            dataStore.edit { preferences ->
                preferences[dataStoreTokenKey] = encrypted
            }
            _tokenFlow.value = token
            logger.i { "Token saved securely" }
        } catch (e: SecureStorageException) {
            throw e
        } catch (e: Exception) {
            logger.e(e) { "Failed to save token" }
            throw SecureStorageException("Failed to save token", e)
        }
    }

    override suspend fun getToken(): String? {
        return try {
            dataStore.data
                .map { preferences ->
                    preferences[dataStoreTokenKey]
                }
                .first()
                ?.let { decrypt(it) }
                .also { _tokenFlow.value = it }
        } catch (e: SecureStorageException) {
            throw e
        } catch (e: Exception) {
            logger.e(e) { "Failed to retrieve token" }
            throw SecureStorageException("Failed to retrieve token", e)
        }
    }

    override suspend fun clearToken() {
        try {
            dataStore.edit { preferences ->
                preferences.remove(dataStoreTokenKey)
            }
            _tokenFlow.value = null
            logger.i { "Token cleared from secure storage" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to clear token" }
            throw SecureStorageException("Failed to clear token", e)
        }
    }
}
