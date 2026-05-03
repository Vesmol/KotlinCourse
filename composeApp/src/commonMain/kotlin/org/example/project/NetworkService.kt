package org.example.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

object NetworkService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchPost(postId: Int): Post {
        return client.get("https://jsonplaceholder.typicode.com/posts/$postId").body()
    }
}