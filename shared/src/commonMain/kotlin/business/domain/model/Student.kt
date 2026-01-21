package business.domain.model


import kotlin.random.Random

data class Student(
    val id: String = Random.nextInt().toString(),
    val fullName: String,
    val nisn: String,
    val birthDate: String,
    val major: Major
)
