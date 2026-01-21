package business.datasource.network.main

import business.domain.model.Student


interface MainService {

    companion object {
        const val SEARCH_FILTER = "search/filter"
        const val SEARCH = "search"
        const val HOME = "home"
        const val PROFILE = "profile"
        const val NOTIFICATIONS = "notifications"
        const val LIST_BUS = "list_bus"
        const val DETAIL_TRANSACTION = "trx_detail"
        const val SUMMARY_TRANSACTION = "trx_summary"
        const val HISTORY_ATTENDANCE = "attendance"
        const val ATTENDANCE = "attendance"
        const val HISTORY_RIT = "history_rit"
        const val RIT = "rit"
        const val ATTENDANCE_QR = "attendance_qr"
        const val TICKET_TRANSACTION = "trx_detail"
        const val HISTORY_QRIS = "history_qris"
        const val QR_BUS = "init_qris"
        const val GOOGLE_LOGIN = "google-login"
        const val NEAREST_HALTE = "tracker/nearest_halte"
        const val ROUTE_LIST = "tracker/route_list"
        const val ROUTE_DETAIL = "tracker/detail_route"
        const val TRIP = "tracker/trip"
        const val MAPS_ROUTE = "tracker/maps_route"



    }

    suspend fun login(username: String, password: String): Boolean
    suspend fun submitStudent(student: Student)
}
