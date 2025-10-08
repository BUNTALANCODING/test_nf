package business.domain.main

import org.jetbrains.compose.resources.DrawableResource

data class Country(
    val name: String,
    val code: String,
    val flagRes: DrawableResource
)