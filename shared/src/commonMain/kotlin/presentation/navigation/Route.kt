package presentation.navigation


sealed class Route {
    data object Splash : Route()
    data object Login : Route()
}
