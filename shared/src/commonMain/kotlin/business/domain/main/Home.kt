package business.domain.main

import business.datasource.network.main.responses.ArticleDTO
import business.datasource.network.main.responses.CargoCategoryDTO
import business.datasource.network.main.responses.City
import business.datasource.network.main.responses.Gender
import business.datasource.network.main.responses.Guide
import business.datasource.network.main.responses.IdentityType
import business.datasource.network.main.responses.PassengerCategory
import business.datasource.network.main.responses.PaymentType
import business.datasource.network.main.responses.Route
import business.datasource.network.main.responses.Setting
import business.datasource.network.main.responses.Time

data class Home(
    val banners: List<Banner> = listOf(),
    val articleType: List<ArticleType> = listOf(),
    val article: List<ArticleDTO> = listOf(),
    var route: List<Route> = listOf(),
    var time: List<Time> = listOf(),
    var cargoCategory: List<CargoCategoryDTO> = listOf(),
    var passengerCategory: List<PassengerCategory> = listOf(),
    var identityType: List<IdentityType> = listOf(),
    var gender: List<Gender> = listOf(),
    var city: List<City> = listOf(),
    var paymentType: List<PaymentType> = listOf(),
    var setting: List<Setting> = listOf(),
    val guide: List<Guide> = listOf(),
)