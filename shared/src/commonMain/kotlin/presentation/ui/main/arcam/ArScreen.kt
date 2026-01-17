package presentation.ui.main.arcam

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import arhud.LatLng
import common.map.CameraPreview
import common.map.KmpMapView
import common.map.RequireArPermissions
import common.map.SensorFix
import common.map.rememberSensorProvider
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import presentation.ui.main.arcam.view_model.HudNavigator
import presentation.ui.main.arcam.view_model.HudState
import presentation.ui.main.arcam.view_model.OsrmApi
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.cos
import kotlin.math.sin

private val MAP_DOCK_HEIGHT = 340.dp

@Composable
fun ArScreen(
    destination: LatLng,
    modifier: Modifier = Modifier
) {
    RequireArPermissions(modifier = modifier) {
        NavHudWithMapContent(destination = destination, modifier = modifier)
    }
}

@Composable
private fun NavHudWithMapContent(
    destination: LatLng,
    modifier: Modifier = Modifier
) {
    val sensor = rememberSensorProvider()

    val http = remember {
        HttpClient {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
    }
    val osrm = remember { OsrmApi(http) }

    var fix by remember { mutableStateOf<SensorFix?>(null) }
    var route by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    val userLatLng = fix?.let { LatLng(it.lat, it.lon) }
    val navigator = remember(route) { HudNavigator(route) }

    DisposableEffect(Unit) {
        sensor.start { fix = it }
        onDispose { sensor.stop() }
    }

    LaunchedEffect(userLatLng?.lat, userLatLng?.lon, destination.lat, destination.lon) {
        val u = userLatLng ?: return@LaunchedEffect
        if (route.isEmpty()) {
            runCatching { route = osrm.routeFoot(u, destination) }
        }
    }

    val hud: HudState = remember(fix, route) {
        val f = fix
        if (f == null) HudState(0f, "Menunggu lokasi...", "--")
        else navigator.update(LatLng(f.lat, f.lon), f.headingDeg)
    }

    Box(modifier.fillMaxSize()) {
        // 1) background camera
        CameraPreview(Modifier.fillMaxSize())

        // 2) map dock (di bawah)
        BottomMapDock(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(MAP_DOCK_HEIGHT)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .zIndex(1f),
            route = route,
            user = userLatLng,
            headingDeg = fix?.headingDeg ?: 0f
        )

        // 3) distance chip (di atas map)
        if (hud.distanceText.isNotBlank() && hud.distanceText != "--") {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = MAP_DOCK_HEIGHT + 10.dp)
                    .zIndex(10f),
                shape = RoundedCornerShape(999.dp),
                color = Color(0x66000000)
            ) {
                Text(
                    text = hud.distanceText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 4) center overlay (PASTI DI ATAS MAP)
        CenterNavOverlay(
            instruction = hud.instruction,
            modifier = Modifier
                .fillMaxSize()
                // ✅ area overlay dipotong setinggi map, jadi ga bakal “masuk” area map
                .padding(bottom = MAP_DOCK_HEIGHT + 10.dp)
                .zIndex(20f)
        )

        // 5) top controls paling atas
        TopOverlayControls(
            onBack = { },
            onMenu = { }
        )
    }
}

/* ---------------- TOP CONTROLS ---------------- */

@Composable
private fun TopOverlayControls(
    onBack: () -> Unit,
    onMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .zIndex(30f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = Color(0x66000000)) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Surface(shape = CircleShape, color = Color(0x66000000)) {
            IconButton(onClick = onMenu) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }
    }
}

/* ---------------- CENTER NAV OVERLAY (Rambu) ---------------- */

private enum class TurnType { STRAIGHT, LEFT, RIGHT, UTURN }

private fun inferTurnType(instruction: String): TurnType {
    val t = instruction.lowercase()
    return when {
        t.contains("putar") || t.contains("u-turn") || t.contains("uturn") || t.contains("balik") -> TurnType.UTURN
        t.contains("kiri") -> TurnType.LEFT
        t.contains("kanan") -> TurnType.RIGHT
        else -> TurnType.STRAIGHT
    }
}

@Composable
private fun CenterNavOverlay(
    instruction: String,
    modifier: Modifier = Modifier
) {
    val turn = remember(instruction) { inferTurnType(instruction) }

    Box(modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF2F6FED),
                shadowElevation = 10.dp
            ) {
                Text(
                    text = instruction.ifBlank { "..." },
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(14.dp))

            // ✅ rambu penunjuk arah (beda bentuk per turn)
            DirectionSign(
                turnType = turn,
                modifier = Modifier.size(150.dp)
            )
        }
    }
}

/**
 * Kotak biru seperti rambu + panah putih (lurus/kanan/kiri/putar balik).
 * Animasi pulse halus biar hidup.
 */
@Composable
private fun DirectionSign(turnType: TurnType, modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition()
    val p by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing))
    )

    // triangle wave 0..1..0
    val wave = 1f - abs(2f * p - 1f)
    val scale = 1f + 0.05f * wave
    val lift = -6f * wave

    Canvas(
        modifier = modifier.graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationY = lift
        )
    ) {
        val w = size.width
        val h = size.height
        val r = min(w, h) * 0.14f

        // background
        drawRoundRect(
            color = Color(0xFF0B57D0),
            cornerRadius = CornerRadius(r, r)
        )
        // border putih
        drawRoundRect(
            color = Color.White,
            cornerRadius = CornerRadius(r, r),
            style = Stroke(width = min(w, h) * 0.06f)
        )

        // arrow stroke
        val stroke = Stroke(
            width = min(w, h) * 0.12f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        fun arrowHead(at: Offset, angleDeg: Float) {
            val len = min(w, h) * 0.18f
            val a = angleDeg * PI.toFloat() / 180f
            val left = (angleDeg + 150f) * PI.toFloat() / 180f
            val right = (angleDeg - 150f) * PI.toFloat() / 180f

            val pth = Path().apply {
                moveTo(at.x, at.y)
                lineTo(at.x + len * cos(left), at.y + len * sin(left))
                moveTo(at.x, at.y)
                lineTo(at.x + len * cos(right), at.y + len * sin(right))
            }
            drawPath(pth, color = Color.White, style = stroke)
        }

        when (turnType) {
            TurnType.STRAIGHT -> {
                val cx = w / 2f
                val from = Offset(cx, h * 0.78f)
                val to = Offset(cx, h * 0.24f)
                val pth = Path().apply {
                    moveTo(from.x, from.y)
                    lineTo(to.x, to.y)
                }
                drawPath(pth, color = Color.White, style = stroke)
                arrowHead(to, -90f)
            }

            TurnType.RIGHT -> {
                val pth = Path().apply {
                    moveTo(w * 0.36f, h * 0.78f)
                    lineTo(w * 0.36f, h * 0.42f)
                    quadraticTo(w * 0.36f, h * 0.26f, w * 0.52f, h * 0.26f)
                    lineTo(w * 0.78f, h * 0.26f)
                }
                drawPath(pth, color = Color.White, style = stroke)
                arrowHead(Offset(w * 0.78f, h * 0.26f), 0f)
            }

            TurnType.LEFT -> {
                val pth = Path().apply {
                    moveTo(w * 0.64f, h * 0.78f)
                    lineTo(w * 0.64f, h * 0.42f)
                    quadraticTo(w * 0.64f, h * 0.26f, w * 0.48f, h * 0.26f)
                    lineTo(w * 0.22f, h * 0.26f)
                }
                drawPath(pth, color = Color.White, style = stroke)
                arrowHead(Offset(w * 0.22f, h * 0.26f), 180f)
            }

            TurnType.UTURN -> {
                // U-turn: naik lalu balik turun
                val pth = Path().apply {
                    moveTo(w * 0.62f, h * 0.82f)
                    lineTo(w * 0.62f, h * 0.46f)
                    quadraticTo(w * 0.62f, h * 0.20f, w * 0.50f, h * 0.20f)
                    quadraticTo(w * 0.38f, h * 0.20f, w * 0.38f, h * 0.46f)
                    lineTo(w * 0.38f, h * 0.64f)
                }
                drawPath(pth, color = Color.White, style = stroke)
                arrowHead(Offset(w * 0.38f, h * 0.64f), 90f)
            }
        }
    }
}

/* ---------------- BOTTOM MAP DOCK ---------------- */

@Composable
private fun BottomMapDock(
    modifier: Modifier,
    route: List<LatLng>,
    user: LatLng?,
    headingDeg: Float
) {
    val shape = remember { CircleSegmentTopShape(rise = 86.dp) }

    Surface(
        modifier = modifier.clip(shape),
        shape = shape,
        shadowElevation = 14.dp,
        tonalElevation = 8.dp,
        color = Color.White
    ) {
        Box(Modifier.fillMaxSize()) {
            KmpMapView(
                modifier = Modifier.fillMaxSize(),
                routePoints = route,
                user = user,
                followUser = true,
                showUserMarker = true,
                userHeadingDeg = headingDeg
            )

            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.White.copy(alpha = 0.18f))
            )
        }
    }
}

private class CircleSegmentTopShape(
    private val rise: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height

        val risePx = with(density) { rise.toPx() }
        val s = min(risePx, h * 0.85f).coerceAtLeast(1f)

        val R = (w * w) / (8f * s) + (s / 2f)
        val cx = w / 2f
        val cy = R

        val rect = Rect(cx - R, 0f, cx + R, 2f * R)

        fun angleDeg(x: Float, y: Float): Float {
            val dx = x - cx
            val dy = y - cy
            return atan2(dy, dx) * 180f / PI.toFloat()
        }

        val a1 = angleDeg(0f, s)
        val a2 = angleDeg(w, s)
        var sweep = a2 - a1
        if (sweep < 0f) sweep += 360f

        val path = Path().apply {
            moveTo(0f, s)
            arcTo(rect, a1, sweep, false)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        return Outline.Generic(path)
    }
}
