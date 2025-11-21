package presentation.ui.main.uploadChunk

import business.core.ViewSingleAction

sealed class UploadAction : ViewSingleAction {

    sealed class Navigation : UploadAction() {

        data object NavigateToMain : UploadAction()

        data object NavigateToLogin : UploadAction()
    }
}