package lt.vitalijus.feature.auth.data.mappers

import lt.vitalijus.core.database.entity.UserEntity
import lt.vitalijus.core.domain.model.Scan
import lt.vitalijus.feature.auth.data.network.LoginResponseDto
import lt.vitalijus.feature.auth.data.network.ScanDto
import lt.vitalijus.feature.auth.domain.LoginResult
import lt.vitalijus.feature.auth.domain.User

fun LoginResponseDto.toDomain(): LoginResult? {
    val loginData = this.login ?: return null
    val userDto = this.user

    val user = User(
        id = loginData.userId,
        email = userDto?.email ?: "",
        name = userDto?.name,
        validTill = loginData.validTill,
        registrationDate = loginData.registrationDate,
        isLoggedIn = true
    )

    val scans = this.scans?.map { it.toDomain() } ?: emptyList()

    return LoginResult(
        user = user,
        token = loginData.token,
        scans = scans
    )
}

fun ScanDto.toDomain(): Scan {
    return Scan(
        id = id,
        lat = lat,
        lon = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        validTill = validTill,
        registrationDate = registrationDate,
        isLoggedIn = isLoggedIn
        // Note: token is stored in SecureStorage, not in Room
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        validTill = validTill,
        registrationDate = registrationDate,
        isLoggedIn = isLoggedIn
    )
}
