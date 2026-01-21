package business.interactors.main

import business.domain.model.Student
import business.domain.repository.StudentRepository


class RegisterStudentUseCase(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(student: Student) {
        require(student.fullName.isNotBlank()) { "Nama wajib diisi" }
        require(student.nisn.length == 10) { "NISN harus 10 digit" }
        repository.register(student)
    }
}
