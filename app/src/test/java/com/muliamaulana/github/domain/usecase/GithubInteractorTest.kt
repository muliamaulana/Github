package com.muliamaulana.github.domain.usecase

import com.muliamaulana.github.MainDispatcherRule
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import com.muliamaulana.github.domain.repository.IGithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

/**
 * Created by muliamaulana on 16/02/25.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class GithubInteractorTest {

    @Mock
    private lateinit var repository: IGithubRepository
    private lateinit var githubInteractor: GithubInteractor

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        githubInteractor = GithubInteractor(repository)
    }

    @Test
    fun `getListUser() should return expected data`() = runTest {
        val fakeUsers = mutableListOf(
            ItemListUser(
                login = "John Doe",
                avatarUrl = "https://avatar.com/johndoe"
            )
        )
        `when`(repository.getListUser()).thenReturn(flowOf(Resource.Success(fakeUsers)))
        val result = githubInteractor.getListUser().toList()
        assert(result[0].data?.size == 1)
        assert(result[0] is Resource.Success && result[0].data == fakeUsers)
    }

    @Test
    fun `getListUser() return error`() = runTest {
        val errorMessage = "Something wrong"
        `when`(repository.getListUser()).thenReturn(flowOf(Resource.Error(errorMessage)))
        val result = githubInteractor.getListUser().toList()
        assert(result[0] is Resource.Error && result[0].message == errorMessage)
    }

    @Test
    fun `getDetailUser() should return expected data`() = runTest {
        val fakeUser = DetailUserResponse(
            login = "JohnDoe",
            avatarUrl = "https://avatar.com/johndoe"
        )
        `when`(repository.getDetailUser(any())).thenReturn(flowOf(Resource.Success(fakeUser)))
        val result = githubInteractor.getDetailUser("JohnDoe").toList()
        assert(result[0] is Resource.Success && result[0].data == fakeUser)
    }

    @Test
    fun `getRepos() should return expected data`() = runTest {
        val fakeRepos = mutableListOf(
            ItemRepos(
                name = "Test Repo",
                description = "Test Repository"
            )
        )
        `when`(repository.getUserRepos(any())).thenReturn(flowOf(Resource.Success(fakeRepos)))
        val result = githubInteractor.getRepos("JohnDoe").toList()
        assert(result[0].data?.size == 1)
    }

    @Test
    fun `getRepos() return error`() = runTest {
        val errorMessage = "Something wrong"
        `when`(repository.getUserRepos(any())).thenReturn(flowOf(Resource.Error(errorMessage)))
        val result = githubInteractor.getRepos("JohnDoe").toList()
        assert(result[0] is Resource.Error && result[0].message == errorMessage)
    }

}