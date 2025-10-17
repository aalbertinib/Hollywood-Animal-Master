package org.aalbertini.ham.core.util.platform

/**
 * Web platform implementation.
 */
class WebPlatform : Platform {
    override val type: PlatformType = PlatformType.WEB
    override val name: String = "Web"
}

actual fun getPlatform(): Platform = WebPlatform()
