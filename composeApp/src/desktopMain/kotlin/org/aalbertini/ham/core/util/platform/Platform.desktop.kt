package org.aalbertini.ham.core.util.platform

/**
 * Desktop platform implementation.
 */
class DesktopPlatform : Platform {
    override val type: PlatformType = PlatformType.DESKTOP
    override val name: String = "Desktop (${System.getProperty("os.name")})"
}

actual fun getPlatform(): Platform = DesktopPlatform()
