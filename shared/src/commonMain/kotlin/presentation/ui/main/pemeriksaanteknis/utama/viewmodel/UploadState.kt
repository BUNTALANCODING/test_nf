package presentation.ui.main.pemeriksaanteknis.utama.viewmodel

data class UploadState(
    val fileName: String = "",
    val isUploading: Boolean = false,
    val progress: Int = 0, // 0..100
    val status: String = "",

    val uniqueKey: String? = null,

    val showLimitDialog: Boolean = false,
    val limitDialogMessage: String = "",

    val isPaused: Boolean = false
)