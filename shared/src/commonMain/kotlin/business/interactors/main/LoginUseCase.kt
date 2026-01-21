package business.interactors.main

import business.domain.repository.StudentRepository


class LoginUseCase(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(username: String, password: String): Boolean {
        require(username.isNotBlank()) { "Username tidak boleh kosong" }
        require(password.isNotBlank()) { "Password tidak boleh kosong" }
        return repository.login(username, password)
    }
}
