package business.domain.main

import org.jetbrains.compose.resources.DrawableResource

data class TicketType(
    val title: String,
    val subtitle: String,
    val description: String?,
    val imageRes: DrawableResource
)