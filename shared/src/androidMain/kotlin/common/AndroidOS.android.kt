package common

import android.os.Build

actual fun AndroidQOrBelow(): Boolean {
    return Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q
}