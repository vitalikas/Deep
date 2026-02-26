package lt.vitalijus.feature.auth.data.mappers

import lt.vitalijus.core.database.entity.UserEntity
import lt.vitalijus.feature.auth.data.network.LoginResponseDto
import lt.vitalijus.feature.auth.data.network.ScanDto
import lt.vitalijus.feature.auth.domain.LoginResult
import lt.vitalijus.feature.auth.domain.ScanInfo
import lt.vitalijus.feature.auth.domain.User

fun LoginResponseDto.toDomain(): LoginResult? {
    val loginData = this.login ?: return null
    val userDto = this.user

    val user = User(
        id = loginData.userId,
        email = userDto?.email ?: "",
        name = userDto?.name,
        token = loginData.token,
        validTill = loginData.validTill,
        registrationDate = loginData.registrationDate,
        isLoggedIn = true
    )

    val scans = this.scans?.map { it.toDomain() } ?: emptyList()

    return LoginResult(
        user = user,
        scans = scans
    )
}

fun ScanDto.toDomain(): ScanInfo {
    return ScanInfo(
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
        token = token,
        validTill = validTill,
        registrationDate = registrationDate,
        isLoggedIn = isLoggedIn
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        token = token,
        validTill = validTill,
        registrationDate = registrationDate,
        isLoggedIn = isLoggedIn
    )
}
