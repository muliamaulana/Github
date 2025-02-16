package com.muliamaulana.github.data

import com.muliamaulana.github.MainDispatcherRule
import com.muliamaulana.github.data.source.remote.RemoteDataSource
import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

/**
 * Created by muliamaulana on 16/02/25.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class GithubRepositoryTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: GithubRepository

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        MockitoAnnotations.openMocks(this)
        repository = GithubRepository(remoteDataSource)
    }

    @Test
    fun `getListUser() returns success`() = runTest {
        val fakeUsers =
            mutableListOf(ItemListUser(login = "JohnDoe", avatarUrl = "https://avatar.com/johndoe"))
        coEvery { remoteDataSource.getListUser() } returns flowOf(ApiResponse.Success(fakeUsers))
        val result = repository.getListUser().toList()

        assert(result[0] is Resource.Loading)
        assert(result[1] is Resource.Success && result[1].data == fakeUsers)
    }

    @Test
    fun `getListUser() returns error`() = runTest {
        val errorMessage = "Something wrong"
        coEvery { remoteDataSource.getListUser() } returns flowOf(ApiResponse.Error(errorMessage))

        val result = repository.getListUser().toList()

        assert(result[0] is Resource.Loading)
        assert(result[1] is Resource.Error && result[1].message == errorMessage)
    }

    @Test
    fun `getDetailUser() returns success`() = runTest {
        val fakeResponse = DetailUserResponse(
            login = "JohnDoe", avatarUrl = "https://avatar.com/johndoe"
        )

        coEvery { remoteDataSource.getDetailUser(any()) } returns flowOf(
            ApiResponse.Success(
                fakeResponse
            )
        )
        val result = repository.getDetailUser("JohnDoe").toList()

        assert(result[0] is Resource.Loading)
        assert(result[1] is Resource.Success && result[1].data == fakeResponse)

    }

    @Test
    fun `getUserRepos() return succes`() = runTest {
        val fakeRepos =
            mutableListOf(ItemRepos(name = "Test Repo", description = "Test Repository"))

        coEvery { remoteDataSource.getUserRepos(any()) } returns  flowOf(
            ApiResponse.Success(fakeRepos)
        )

        val result = repository.getUserRepos("JohnDoe").toList()

        assert(result[0] is Resource.Loading)
        assert(result[1].data?.size == 1)
        assert(result[1] is Resource.Success && result[1].data == fakeRepos)


    }
}