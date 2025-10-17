package org.aalbertini.ham.core.util.platform

/**
 * Android platform implementation.
 */
class AndroidPlatform : Platform {
    override val type: PlatformType = PlatformType.ANDROID
    override val name: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()
