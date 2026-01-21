package presentation.ui.main.register.state

import business.domain.model.Student

sealed class StudentState {
    data object Idle : StudentState()
    data object Loading : StudentState()
    data class Error(val message: String) : StudentState()
    data class Data(val students: List<Student>) : StudentState()
    data object RegisterSuccess : StudentState()
}
