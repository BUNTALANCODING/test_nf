package business.interactors.main

import business.domain.repository.StudentRepository


class GetStudentsUseCase(
    private val repository: StudentRepository
) {
    suspend operator fun invoke() = repository.getStudents()
}
