package org.aalbertini.ham.core.util.platform

import platform.UIKit.UIDevice

/**
 * iOS platform implementation.
 */
class IOSPlatform : Platform {
    override val type: PlatformType = PlatformType.IOS
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
