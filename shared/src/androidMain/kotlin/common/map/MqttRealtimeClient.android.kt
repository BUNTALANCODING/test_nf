package common.map

// androidMain
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.map.RealtimeBusDto

actual class MqttRealtimeClient {

    private var client: MqttClient? = null
    private val json = Json { ignoreUnknownKeys = true }

    actual fun subscribe(topic: String): Flow<List<BusMapUi>> = callbackFlow {

        val mqttClient = MqttClient(
            "tcp://mqtt.jsa2.host:12345",
            MqttClient.generateClientId(),
            MemoryPersistence()
        )

        client = mqttClient

        val options = MqttConnectOptions().apply {
            userName = "server"
            password = "1sampai8".toCharArray()
            isAutomaticReconnect = true
            isCleanSession = true
        }

        mqttClient.connect(options)

        mqttClient.subscribe(topic) { _, message ->
            val payload = message.payload.decodeToString()

            val dto = json.decodeFromString<RealtimeBusDto>(payload)

            val bus = BusMapUi(
                id = dto.busCode,
                position = GeoPoint(dto.lat, dto.lng),
                code = dto.busCode,
                destination = dto.nextHalte,
                eta = dto.eta
            )

            trySend(listOf(bus))
        }

        awaitClose {
            mqttClient.disconnect()
        }
    }

    actual fun disconnect() {
        client?.takeIf { it.isConnected }?.disconnect()
    }
}
