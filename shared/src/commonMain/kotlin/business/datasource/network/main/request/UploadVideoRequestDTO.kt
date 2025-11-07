package business.datasource.network.main.request

data class UploadVideoRequestDTO(
    val filePath: String,
    val onProgress: (Float) -> Unit
)