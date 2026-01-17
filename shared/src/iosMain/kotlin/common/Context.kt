package common

import platform.Foundation.NSUserDefaults

actual class Context(
    val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()
)
