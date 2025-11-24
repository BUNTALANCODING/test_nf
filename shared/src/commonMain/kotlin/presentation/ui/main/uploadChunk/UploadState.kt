package presentation.ui.main.uploadChunk


data class UploadState(
    val fileName: String = "",
    val isUploading: Boolean = false,
    val progress: Int = 0, // 0..100
    val status: String = "",

    val uniqueKey: String? = null
)

