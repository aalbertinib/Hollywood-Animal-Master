package org.aalbertini.ham.core.util.platform

/**
 * Platform types supported by the application.
 */
enum class PlatformType {
    ANDROID,
    IOS,
    DESKTOP,
    WEB
}

/**
 * Platform information and capabilities.
 */
interface Platform {
    /**
     * The type of platform
     */
    val type: PlatformType
    
    /**
     * Platform name (e.g., "Android", "iOS", "Desktop", "Web")
     */
    val name: String
    
    /**
     * Whether this is a mobile platform (Android or iOS)
     */
    val isMobile: Boolean
        get() = type == PlatformType.ANDROID || type == PlatformType.IOS
    
    /**
     * Whether this is a desktop platform
     */
    val isDesktop: Boolean
        get() = type == PlatformType.DESKTOP
    
    /**
     * Whether this is a web platform
     */
    val isWeb: Boolean
        get() = type == PlatformType.WEB
}

/**
 * Get the current platform information.
 */
expect fun getPlatform(): Platform
