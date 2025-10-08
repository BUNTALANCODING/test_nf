package business.constants

enum class OrderStatus {
    Completed,
    Pending,
    Cancelled,
    All
}

object TrxStatus {
    const val ALL = 0
    const val NOT_PAYMENT = 1
    const val ACTIVATED = 2
    const val HISTORY = 3
    const val FINISHED = 4
}

