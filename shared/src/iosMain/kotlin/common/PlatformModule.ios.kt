package common

import iosModule
import org.koin.core.module.Module

actual fun platformModule(): Module {
    return iosModule
}