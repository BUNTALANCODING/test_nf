package presentation.ui.main.arcam.view_model

import arhud.LatLng
import arhud.bearingDeg
import arhud.haversineMeters
import arhud.lerpAngle
import arhud.norm180


data class HudState(
    val arrowAngleDeg: Float, // -180..180
    val instruction: String,
    val distanceText: String
)

class HudNavigator(
    private val route: List<LatLng>,
    private val lookaheadMeters: Double = 15.0
) {
    private var lastNearestIdx = 0
    private var headingFiltered = 0f

    fun update(user: LatLng, headingDeg: Float): HudState {
        headingFiltered = lerpAngle(headingFiltered, headingDeg, 0.15f)

        if (route.size < 2) {
            return HudState(0f, "Menunggu rute...", "--")
        }

        val target = lookaheadPoint(user, route, lookaheadMeters)
        val bearing = bearingDeg(user, target)
        val turn = norm180(bearing - headingFiltered)

        val dist = haversineMeters(user, target).toInt().coerceAtLeast(0)

        return HudState(
            arrowAngleDeg = turn,
            instruction = instructionFromTurn(turn),
            distanceText = "${dist} m"
        )
    }

    private fun instructionFromTurn(turn: Float): String {
        val a = kotlin.math.abs(turn)
        return when {
            a < 15f -> "Lurus"
            a > 135f -> "Putar balik"
            turn > 0f -> "Belok kanan"
            else -> "Belok kiri"
        }
    }

    private fun lookaheadPoint(user: LatLng, pts: List<LatLng>, lookaheadM: Double): LatLng {
        val i0 = findNearestIndex(user, pts)
        var acc = 0.0
        var i = i0
        while (i < pts.lastIndex && acc < lookaheadM) {
            acc += haversineMeters(pts[i], pts[i + 1])
            i++
        }
        return pts[i]
    }

    private fun findNearestIndex(user: LatLng, pts: List<LatLng>): Int {
        val start = (lastNearestIdx - 20).coerceAtLeast(0)
        val end = (lastNearestIdx + 50).coerceAtMost(pts.lastIndex)

        var bestIdx = start
        var best = Double.MAX_VALUE
        for (i in start..end) {
            val d = haversineMeters(user, pts[i])
            if (d < best) { best = d; bestIdx = i }
        }
        lastNearestIdx = bestIdx
        return bestIdx
    }
}
