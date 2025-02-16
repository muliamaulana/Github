package com.muliamaulana.github.data.source.remote

import com.muliamaulana.github.data.source.remote.network.GithubApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by muliamaulana on 16/02/25.
 */

@ExperimentalCoroutinesApi
class GithubApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: GithubApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(GithubApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getListUser() returns success response`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                [
                  {"login": "JohnDoe", "avatar_url": "https://avatar.com/johndoe"}
                ]
            """.trimIndent()
            )

        mockWebServer.enqueue(mockResponse)

        val response = apiService.getListUser()
        assert(response.isSuccessful)
        assert(response.body()?.size == 1)
        assert(response.body()?.get(0)?.login == "JohnDoe")
    }

    @Test
    fun `getListUser() returns error response`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(403)
            .setBody("""
                {
                  "message": "API rate limit exceeded",
                  "status": "403"
                }
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        val response = apiService.getListUser()
        assert(!response.isSuccessful)
        assert(response.code() == 403)
    }

}