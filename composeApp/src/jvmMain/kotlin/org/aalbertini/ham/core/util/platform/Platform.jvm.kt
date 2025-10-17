package org.aalbertini.ham.core.util.platform

/**
 * JVM platform implementation (Desktop).
 */
class JvmPlatform : Platform {
    override val type: PlatformType = PlatformType.DESKTOP
    override val name: String = "Desktop (${System.getProperty("os.name")})"
}

actual fun getPlatform(): Platform = JvmPlatform()
