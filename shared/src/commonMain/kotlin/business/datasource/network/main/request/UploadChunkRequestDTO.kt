package business.datasource.network.main.request

import kotlinx.serialization.Serializable

@Serializable
data class UploadChunkRequestDTO(
    val uniqueKey: String,
    val fileName: String,
    val chunkIndex: Int,
    val totalChunks: Int,
    val file: ByteArray
)
