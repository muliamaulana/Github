package com.muliamaulana.github.data.source.remote

import com.muliamaulana.github.MainDispatcherRule
import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.network.GithubApiService
import com.muliamaulana.github.data.source.remote.network.SafeApiCall
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/**
 * Created by muliamaulana on 16/02/25.
 */

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    private lateinit var apiService: GithubApiService
    private lateinit var safeApiCall: SafeApiCall
    private lateinit var remoteDataSource: RemoteDataSource

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Before
    fun setUp() {
        safeApiCall = mockk(relaxed = true)
        apiService = mockk(relaxed = true)

        remoteDataSource = RemoteDataSource(safeApiCall, apiService)
    }

    @Test
    fun `getListUser() returns success`() = runTest {

        val expectedData = mutableListOf(ItemListUser(id = 1, login = "testUser"))
        val mockResponse: Response<MutableList<ItemListUser>> = Response.success(expectedData)

        coEvery { safeApiCall.execute(any<suspend () -> Response<MutableList<ItemListUser>>>()) } returns ApiResponse.Success(
            expectedData
        )

        val result = safeApiCall.execute { mockResponse }

        assert(result is ApiResponse.Success)
        assertEquals(expectedData, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getListUser() return error`() = runTest {
        val errorMessage = "API rate limit exceeded"
        val errorResponse = ApiResponse.Error(errorMessage)

        coEvery { safeApiCall.execute(any<suspend () -> Response<MutableList<ItemListUser>>>())} returns errorResponse
        val result = remoteDataSource.getListUser().first()

        assertTrue(result is ApiResponse.Error)
        assertEquals(errorMessage, (result as ApiResponse.Error).errorMessage)
    }


}