package org.aalbertini.ham

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform