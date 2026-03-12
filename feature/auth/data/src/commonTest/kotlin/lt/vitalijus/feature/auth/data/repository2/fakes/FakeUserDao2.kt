package lt.vitalijus.feature.auth.data.repository2.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import lt.vitalijus.core.database.dao.UserDao
import lt.vitalijus.core.database.entity.UserEntity

class FakeUserDao2(
    private val shouldInsertFail: Boolean = false,
    private val shouldClearFail: Boolean = false
) : UserDao {

    private val users = MutableStateFlow<UserEntity?>(null)

    var wasCleared: Boolean = false
        private set

    var insertedUser: UserEntity? = null
        private set

    override suspend fun insertUser(user: UserEntity) {
        if (shouldInsertFail) throw Exception("DB insert failed")
        insertedUser = user
        users.value = user
    }

    override fun getCurrentUser(): Flow<UserEntity?> = users

    override suspend fun getCurrentUserSync(): UserEntity? = users.value

    override suspend fun logout(userId: Long) {
        users.value = users.value?.copy(isLoggedIn = false)
    }

    override suspend fun clearAllUsers() {
        if (shouldClearFail) throw Exception("DB clear failed")
        users.value = null
        insertedUser = null
        wasCleared = true
    }
}
