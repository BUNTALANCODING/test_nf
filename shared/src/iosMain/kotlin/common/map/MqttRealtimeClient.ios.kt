//// iosMain/common/map/MqttRealtimeClient.ios.kt
//package common.map
//
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.serialization.json.Json
//import presentation.ui.main.inforute.view_model.BusMapUi
//import presentation.ui.main.inforute.view_model.GeoPoint
//import platform.darwin.NSObject
//import presentation.ui.main.inforute.view_model.map.RealtimeBusDto
//
//private class Delegate(
//    val onPayload: (String) -> Unit
//) : NSObject(), MqttRealtimeDelegate {
//
//    override fun onMessage(payload: String) {
//        onPayload(payload)
//    }
//}
//
//actual class MqttRealtimeClient {
//
//    private val wrapper = MqttRealtimeWrapper()
//    private val json = Json { ignoreUnknownKeys = true }
//
//    actual fun subscribe(topic: String): Flow<List<BusMapUi>> = callbackFlow {
//
//        val delegate = Delegate { payload ->
//            val dto = json.decodeFromString<RealtimeBusDto>(payload)
//
//            trySend(
//                listOf(
//                    BusMapUi(
//                        id = dto.busCode,
//                        code = dto.busCode,
//                        position = GeoPoint(dto.lat, dto.lng),
//                        destination = dto.nextHalte,
//                        eta = dto.eta
//                    )
//                )
//            )
//        }
//
//        wrapper.delegate = delegate
//        wrapper.connect(topic)
//
//        awaitClose {
//            wrapper.disconnect()
//        }
//    }
//
//    actual fun disconnect() {
//        wrapper.disconnect()
//    }
//}
