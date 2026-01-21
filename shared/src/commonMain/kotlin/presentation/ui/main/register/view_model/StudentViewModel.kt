package presentation.ui.main.register.view_model

import business.domain.model.Student
import business.interactors.main.GetStudentsUseCase
import business.interactors.main.LoginUseCase
import business.interactors.main.RegisterStudentUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.ui.main.register.state.StudentState

class StudentViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterStudentUseCase,
    private val getStudentsUseCase: GetStudentsUseCase
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state: MutableStateFlow<StudentState> =
        MutableStateFlow(StudentState.Idle)

    val state: StateFlow<StudentState> = _state

    fun onLogin(u: String, p: String) {
        scope.launch {
            try {
                _state.value = StudentState.Loading
                val success: Boolean = loginUseCase(u, p)
                _state.value =
                    if (success) StudentState.Idle
                    else StudentState.Error("Login gagal")
            } catch (e: Exception) {
                _state.value = StudentState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun onRegister(s: Student) {
        scope.launch {
            try {
                _state.value = StudentState.Loading
                registerUseCase(s)
                _state.value = StudentState.RegisterSuccess
            } catch (e: Exception) {
                _state.value = StudentState.Error(e.message ?: "Gagal menyimpan data")
            }
        }
    }

    fun loadStudents() {
        scope.launch {
            try {
                _state.value = StudentState.Loading
                val students = getStudentsUseCase()
                _state.value = StudentState.Data(students)
            } catch (e: Exception) {
                _state.value = StudentState.Error(e.message ?: "Gagal memuat data")
            }
        }
    }
}

