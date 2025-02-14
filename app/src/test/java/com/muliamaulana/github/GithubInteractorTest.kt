package com.muliamaulana.github

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muliamaulana.github.data.GithubRepository
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.RemoteDataSource
import com.muliamaulana.github.data.source.remote.network.GithubApiService
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.utils.DataDummy.generateDummyResponse
import com.muliamaulana.github.utils.ExceptionHandler
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by muliamaulana on 14/02/25.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class GithubInteractorTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: GithubApiService

    private lateinit var exceptionHandler: ExceptionHandler
    private lateinit var dataSource: RemoteDataSource
    private lateinit var repository: GithubRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApiService::class.java)

        exceptionHandler = mock()
        dataSource = RemoteDataSource(exceptionHandler, apiService)
        repository = GithubRepository(dataSource)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testBerhasil() = runTest {
        val dummyUser = generateDummyResponse()
        val expectedResult: Flow<Resource<MutableList<ItemListUser>>> =
            flow { emit(Resource.Success(dummyUser)) }
        whenever(repository.getListUser()).thenReturn(expectedResult)

        assertEquals(2, expectedResult.last().data?.size)
//        assertEquals("John Doe", result[0].name)

    }
}