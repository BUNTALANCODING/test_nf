package business.datasource.network.main

import business.core.AppDataStore
import business.domain.model.Student
import io.ktor.client.HttpClient

class MainServiceImpl(
    private val client: HttpClient,
    private val dataStore: AppDataStore
) : MainService {

    override suspend fun login(username: String, password: String): Boolean {
        return username == "admin" && password == "admin"
    }

    override suspend fun submitStudent(student: Student) {
        // mock API (no-op)
    }


}
