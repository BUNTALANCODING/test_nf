package common


expect class FileContainer(
    context: Any?,
    rawUri: String
) {
    val fileName: String
    val size: Long

    suspend fun forEachChunk(
        chunkSize: Int = 1_000_000,
        block: suspend (chunk: ByteArray) -> Unit
    )
}
