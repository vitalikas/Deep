package lt.vitalijus.feature.auth.data.repository2

import kotlinx.coroutines.test.runTest
import lt.vitalijus.core.domain.util.DataError
import lt.vitalijus.core.domain.util.Result
import lt.vitalijus.feature.auth.data.repository2.fakes.FakeAuthApiService2
import lt.vitalijus.feature.auth.data.repository2.fakes.FakeDeepLogger2
import lt.vitalijus.feature.auth.data.repository2.fakes.FakeScanRepository2
import lt.vitalijus.feature.auth.data.repository2.fakes.FakeTokenStorage2
import lt.vitalijus.feature.auth.data.repository2.fakes.FakeUserDao2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryImpl2Test {

    private fun createRepository(
        apiService: FakeAuthApiService2 = FakeAuthApiService2(success = true),
        userDao: FakeUserDao2 = FakeUserDao2(),
        tokenStorage: FakeTokenStorage2 = FakeTokenStorage2(),
        scanRepository: FakeScanRepository2 = FakeScanRepository2(),
        logger: FakeDeepLogger2 = FakeDeepLogger2()
    ) = AuthRepositoryImpl2(
        apiService = apiService,
        userDao = userDao,
        tokenStorage = tokenStorage,
        scanRepository = scanRepository,
        logger = logger
    )

    @Test
    fun `successful login returns success with login result`() = runTest {
        val repository = createRepository()

        val result = repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertIs<Result.Success<Unit>>(result)
    }

    @Test
    fun `successful login saves user to database`() = runTest {
        val userDao = FakeUserDao2()
        val repository = createRepository(userDao = userDao)

        repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertNotNull(userDao.insertedUser)
        assertEquals("test@test.com", userDao.insertedUser?.email)
    }

    @Test
    fun `successful login saves token to storage`() = runTest {
        val tokenStorage = FakeTokenStorage2()
        val repository = createRepository(tokenStorage = tokenStorage)

        repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertEquals("fake-token-123", tokenStorage.savedToken)
    }

    @Test
    fun `successful login saves scans`() = runTest {
        val scanRepository = FakeScanRepository2()
        val repository = createRepository(scanRepository = scanRepository)

        repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertEquals(1, scanRepository.savedScans.size)
    }

    @Test
    fun `api failure returns error`() = runTest {
        val repository = createRepository(
            apiService = FakeAuthApiService2(success = false)
        )

        val result = repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertIs<Result.Failure<DataError>>(result)
        assertEquals(DataError.Remote.UNAUTHORIZED, result.error)
    }

    @Test
    fun `token save failure rolls back user and returns error`() = runTest {
        val userDao = FakeUserDao2()
        val repository = createRepository(
            userDao = userDao,
            tokenStorage = FakeTokenStorage2(saveSuccess = false)
        )

        val result = repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertIs<Result.Failure<DataError>>(result)
        assertTrue(userDao.wasCleared) // user was rolled back
    }

    @Test
    fun `token save failure does not save scans`() = runTest {
        val scanRepository = FakeScanRepository2()
        val repository = createRepository(
            tokenStorage = FakeTokenStorage2(saveSuccess = false),
            scanRepository = scanRepository
        )

        repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertEquals(0, scanRepository.savedScans.size) // scans NOT saved
    }

    @Test
    fun `api failure does not save user or token`() = runTest {
        val userDao = FakeUserDao2()
        val tokenStorage = FakeTokenStorage2()
        val repository = createRepository(
            apiService = FakeAuthApiService2(success = false),
            userDao = userDao,
            tokenStorage = tokenStorage
        )

        repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertNull(userDao.insertedUser)   // user NOT saved
        assertNull(tokenStorage.savedToken) // token NOT saved
    }

    @Test
    fun `database insert failure returns error`() = runTest {
        val failingUserDao = FakeUserDao2(shouldInsertFail = true) // throws exception
        val repository = createRepository(userDao = failingUserDao)

        val result = repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertIs<Result.Failure<DataError>>(result)
    }

    @Test
    fun `clear users failure during rollback is handled`() = runTest {
        val repository = createRepository(
            userDao = FakeUserDao2(shouldClearFail = true),
            tokenStorage = FakeTokenStorage2(saveSuccess = false) // triggers rollback
        )

        val result = repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertIs<Result.Failure<DataError>>(result)
    }

    @Test
    fun `database insert failure does not save token`() = runTest {
        val tokenStorage = FakeTokenStorage2()
        val repository = createRepository(
            userDao = FakeUserDao2(shouldInsertFail = true),
            tokenStorage = tokenStorage
        )

        repository.login(
            email = "test@test.com",
            password = "pass123"
        )

        assertNull(tokenStorage.savedToken)  // token NOT saved
    }
}
