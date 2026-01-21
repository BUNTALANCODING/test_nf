package business.data.local

import business.domain.model.Student


class StudentLocalDataSource {

    private val students = mutableListOf<Student>()

    fun save(student: Student) {
        students.add(student)
    }

    fun getAll(): List<Student> = students
}
