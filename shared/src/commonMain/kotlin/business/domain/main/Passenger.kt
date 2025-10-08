package business.domain.main

data class PassengerCounts(
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0,
    val seniors: Int = 0
)

fun PassengerCounts.totalCount(): Int {
    return adults + children + infants + seniors
}

data class Passenger(
    var passengerCategoryId: Int,
    var passengerCategoryCode: String,
    var passengerCategoryName: String,
    var passengerCategoryQty: Int
)