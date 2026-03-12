package lt.vitalijus.feature.auth.data.mappers2

import lt.vitalijus.feature.auth.data.network2.LoginResponseDto2
import lt.vitalijus.feature.auth.data.network2.ScanDto2
import lt.vitalijus.feature.auth.domain.LoginResult
import lt.vitalijus.feature.auth.domain.User

fun LoginResponseDto2.toDomain(): LoginResult? {
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

fun ScanDto2.toDomain(): lt.vitalijus.core.domain.model.Scan {
    return lt.vitalijus.core.domain.model.Scan(
        id = id,
        lat = lat,
        lon = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}
