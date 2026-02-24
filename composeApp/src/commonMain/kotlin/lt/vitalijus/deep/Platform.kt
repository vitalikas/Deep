package lt.vitalijus.deep

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform