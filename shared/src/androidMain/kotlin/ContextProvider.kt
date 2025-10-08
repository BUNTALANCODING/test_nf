import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import tech.kotlinlang.permission.PermissionInitiation

// In your Android-specific module
@SuppressLint("StaticFieldLeak")
object ContextProvider {
    lateinit var context: Context
        private set

    fun initialize(context: Context) {
        this.context = context
    }

    fun initializePermission(context: ComponentActivity) {
        PermissionInitiation.setActivity(context)
    }

    fun clearPermission() {
        PermissionInitiation.clear()
    }
}
