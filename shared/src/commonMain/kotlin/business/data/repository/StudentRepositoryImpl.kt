package business.data.repository

import business.data.local.StudentLocalDataSource
import business.datasource.network.main.MainService
import business.domain.model.Student
import business.domain.repository.StudentRepository


class StudentRepositoryImpl(
    private val service: MainService,
    private val local: StudentLocalDataSource
) : StudentRepository {

    override suspend fun login(username: String, password: String): Boolean {
        return service.login(username, password)
    }

    override suspend fun register(student: Student) {
        service.submitStudent(student)
        local.save(student)
    }

    override suspend fun getStudents(): List<Student> {
        return local.getAll()
    }
}
