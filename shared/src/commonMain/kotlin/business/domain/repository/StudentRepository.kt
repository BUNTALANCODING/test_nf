package business.domain.repository

import business.domain.model.Student

interface StudentRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun register(student: Student)
    suspend fun getStudents(): List<Student>
}
