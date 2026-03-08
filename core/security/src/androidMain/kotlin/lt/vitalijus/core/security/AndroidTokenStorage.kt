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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lt.vitalijus.core.domain.util.Result
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
 */
class AndroidTokenStorage(
    context: Context,
    private val logger: Logger
) : TokenStorage {

    private val dataStore: DataStore<Preferences> = context.dataStore
    private val dataStoreTokenKey = stringPreferencesKey(KEY_TOKEN)
    private val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)

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
        }
    }

    private fun getSecretKey(): Result<SecretKey, StorageError> {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
            val key = keyStore.getKey(KEYSTORE_ALIAS, null) as? SecretKey
            if (key != null) {
                Result.Success(key)
            } else {
                Result.Failure(StorageError.EncryptionFailed(Exception("Key not found in Keystore")))
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to get secret key" }
            Result.Failure(StorageError.EncryptionFailed(e))
        }
    }

    private fun encrypt(plaintext: String): Result<String, StorageError> {
        return when (val keyResult = getSecretKey()) {
            is Result.Success -> {
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, keyResult.data)
                    val iv = cipher.iv
                    val encrypted = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

                    val combined = ByteArray(iv.size + encrypted.size)
                    System.arraycopy(iv, 0, combined, 0, iv.size)
                    System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)

                    Result.Success(Base64.encodeToString(combined, Base64.NO_WRAP))
                } catch (e: Exception) {
                    logger.e(e) { "Encryption failed" }
                    Result.Failure(StorageError.EncryptionFailed(e))
                }
            }

            is Result.Failure -> Result.Failure(keyResult.error)
        }
    }

    private fun decrypt(ciphertext: String): Result<String, StorageError> {
        return when (val keyResult = getSecretKey()) {
            is Result.Success -> {
                try {
                    val combined = Base64.decode(ciphertext, Base64.NO_WRAP)

                    val iv = ByteArray(GCM_IV_LENGTH)
                    val encrypted = ByteArray(combined.size - GCM_IV_LENGTH)
                    System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH)
                    System.arraycopy(combined, GCM_IV_LENGTH, encrypted, 0, encrypted.size)

                    cipher.init(
                        Cipher.DECRYPT_MODE,
                        keyResult.data,
                        GCMParameterSpec(GCM_TAG_LENGTH, iv)
                    )
                    Result.Success(String(cipher.doFinal(encrypted), Charsets.UTF_8))
                } catch (e: Exception) {
                    logger.e(e) { "Decryption failed" }
                    Result.Failure(StorageError.DecryptionFailed(e))
                }
            }

            is Result.Failure -> Result.Failure(keyResult.error)
        }
    }

    override suspend fun hasToken(): Result<Boolean, StorageError> {
        return try {
            val exists = dataStore.data
                .map { preferences -> preferences.contains(dataStoreTokenKey) }
                .first()
            Result.Success(exists)
        } catch (e: Exception) {
            logger.e(e) { "Failed to check token existence" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun saveToken(token: String): Result<Unit, StorageError> {
        return try {
            when (val encryptedResult = encrypt(token)) {
                is Result.Success -> {
                    dataStore.edit { preferences ->
                        preferences[dataStoreTokenKey] = encryptedResult.data
                    }
                    logger.i { "Token saved securely" }
                    Result.Success(Unit)
                }

                is Result.Failure -> Result.Failure(encryptedResult.error)
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to save token" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun getToken(): Result<String, StorageError> {
        return try {
            val encrypted = dataStore.data
                .map { preferences -> preferences[dataStoreTokenKey] }
                .first()

            if (encrypted == null) {
                return Result.Failure(StorageError.NotFound)
            }

            when (val decryptedResult = decrypt(encrypted)) {
                is Result.Success -> Result.Success(decryptedResult.data)
                is Result.Failure -> Result.Failure(decryptedResult.error)
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to retrieve token" }
            Result.Failure(StorageError.IOError(e))
        }
    }

    override suspend fun clearToken(): Result<Unit, StorageError> {
        return try {
            dataStore.edit { preferences ->
                preferences.remove(dataStoreTokenKey)
            }
            logger.i { "Token cleared from secure storage" }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Failed to clear token" }
            Result.Failure(StorageError.IOError(e))
        }
    }
}
