package lt.vitalijus.feature.auth.data.mappers2

import lt.vitalijus.feature.auth.data.mappers2.toDomain
import lt.vitalijus.feature.auth.data.network2.LoginDataDto2
import lt.vitalijus.feature.auth.data.network2.LoginResponseDto2
import lt.vitalijus.feature.auth.data.network2.ScanDto2
import lt.vitalijus.feature.auth.data.network2.UserDto2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthMappers2Test {

    @Test
    fun `toDomain returns null when login data is null`() {
        val dto2 = LoginResponseDto2(
            login = null,
            user = null,
            scans = null
        )

        val result = dto2.toDomain()

        assertNull(result)
    }

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto2 = LoginResponseDto2(
            login = LoginDataDto2(
                token = "abc123",
                userId = 1L,
                validated = true
            ),
            user = UserDto2(
                id = 1L,
                email = "test@test.com",
                name = "John"
            ),
            scans = listOf(
                ScanDto2(
                    id = 1L,
                    lat = 54.0,
                    lon = 25.0,
                    scanPoints = 100,
                    mode = 1
                )
            )
        )

        val result = dto2.toDomain()

        assertNotNull(result)
        assertEquals("abc123", result.token)
        assertEquals(1L, result.user.id)
        assertEquals("test@test.com", result.user.email)
        assertEquals("John", result.user.name)
        assertEquals(1, result.scans.size)
    }

    @Test
    fun `toDomain handles null scans as empty list`() {
        val dto2 = LoginResponseDto2(
            login = LoginDataDto2(
                token = "abc",
                userId = 1L,
                validated = true
            ),
            user = null,
            scans = null
        )

        val result = dto2.toDomain()

        assertNotNull(result)
        assertEquals(emptyList(), result.scans)
    }
}
