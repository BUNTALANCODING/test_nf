package presentation.navigation

import org.jetbrains.compose.resources.DrawableResource
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bottom_navigation_active
import rampcheck.shared.generated.resources.ic_bottom_notification
import rampcheck.shared.generated.resources.ic_home
import rampcheck.shared.generated.resources.ic_home_active
import rampcheck.shared.generated.resources.ic_my_ticket
import rampcheck.shared.generated.resources.ic_my_ticket_active
import rampcheck.shared.generated.resources.ic_user
import rampcheck.shared.generated.resources.ic_user_active

sealed class BottomNavigation (
    val route: String,
    val title: String,
    val selectedIcon: DrawableResource,
    val unSelectedIcon: DrawableResource,
) {

   data object Home : BottomNavigation(
        route = "Home", title = "Beranda",
        selectedIcon = Res.drawable.ic_home_active,
        unSelectedIcon = Res.drawable.ic_home
    )

   /*data object History : BottomNavigation(
        route = "History", title = "Riwayat",
        selectedIcon = Res.drawable.history,
        unSelectedIcon = Res.drawable.bookmark_border
    )*/

   data object Notification : BottomNavigation(
        route = "Notification", title = "Notifikasi",
        selectedIcon = Res.drawable.ic_bottom_navigation_active,
        unSelectedIcon = Res.drawable.ic_bottom_notification
    )

   data object Profile : BottomNavigation(
        route = "Profile", title = "Profil",
        selectedIcon = Res.drawable.ic_user_active,
        unSelectedIcon = Res.drawable.ic_user
    )

}

