package common.map

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.os.Looper
import android.view.Surface
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import arhud.lerpAngle
import com.google.android.gms.location.*

@Composable
actual fun rememberSensorProvider(): SensorProvider {
    val ctx = LocalContext.current
    return remember { SensorProvider(ctx.applicationContext) }
}

actual class SensorProvider(private val appCtx: Context) {
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(appCtx) }
    private val sensorManager by lazy { appCtx.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val rotSensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }

    private var onUpdate: ((SensorFix) -> Unit)? = null
    private var lastLat = 0.0
    private var lastLon = 0.0
    private var azimuthFiltered = 0f

    private val rotMat = FloatArray(9)
    private val adjMat = FloatArray(9)
    private val orient = FloatArray(3)

    private val locCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val loc = result.lastLocation ?: return
            lastLat = loc.latitude
            lastLon = loc.longitude
            onUpdate?.invoke(SensorFix(lastLat, lastLon, azimuthFiltered))
        }
    }

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(e: SensorEvent) {
            SensorManager.getRotationMatrixFromVector(rotMat, e.values)

            val rotation = currentRotation(appCtx)
            val (axisX, axisY) = when (rotation) {
                Surface.ROTATION_0 -> SensorManager.AXIS_X to SensorManager.AXIS_Y
                Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
                Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
                else -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
            }
            SensorManager.remapCoordinateSystem(rotMat, axisX, axisY, adjMat)
            SensorManager.getOrientation(adjMat, orient)

            val az = Math.toDegrees(orient[0].toDouble()).toFloat()
            val azDeg = (az + 360f) % 360f
            azimuthFiltered = lerpAngle(azimuthFiltered, azDeg, 0.15f)

            onUpdate?.invoke(SensorFix(lastLat, lastLon, azimuthFiltered))
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    @SuppressLint("MissingPermission")
    actual fun start(onUpdate: (SensorFix) -> Unit) {
        this.onUpdate = onUpdate
        sensorManager.registerListener(sensorListener, rotSensor, SensorManager.SENSOR_DELAY_GAME)

        val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500L)
            .setMinUpdateDistanceMeters(1f)
            .build()
        fused.requestLocationUpdates(req, locCallback, Looper.getMainLooper())
    }

    actual fun stop() {
        fused.removeLocationUpdates(locCallback)
        sensorManager.unregisterListener(sensorListener)
        onUpdate = null
    }
}

private fun currentRotation(ctx: Context): Int {
    val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    @Suppress("DEPRECATION") return wm.defaultDisplay.rotation
}
