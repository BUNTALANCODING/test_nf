package common

import androidModule
import org.koin.core.module.Module

actual fun platformModule(): Module {
    return androidModule
}