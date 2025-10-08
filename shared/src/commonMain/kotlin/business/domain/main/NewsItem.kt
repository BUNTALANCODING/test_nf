package business.domain.main

import org.jetbrains.compose.resources.DrawableResource

data class NewsItem(
    val date: String,
    val title: String,
    val imageUrl: DrawableResource,
    val subtitle: String? = null
)