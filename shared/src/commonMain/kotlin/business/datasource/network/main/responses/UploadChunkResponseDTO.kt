package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadChunkResponseDTO(
    @SerialName("unique_key") val uniqueKey: String = "",
    @SerialName("chunk_index") val chunkIndex: Int = 0
)

@Serializable
data class ChunkResponse(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: ChunkData? = null
)

@Serializable
data class ChunkData(
    @SerialName("unique_key")
    val uniqueKey: String?,
    @SerialName("chunk_index")
    val chunkIndex: String?
)

