package com.muliamaulana.github.data.source.remote

import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.network.GithubApiService
import com.muliamaulana.github.data.source.remote.network.SafeApiCall
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import com.muliamaulana.github.data.source.remote.response.SearchUserResponse
import com.muliamaulana.github.utils.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by muliamaulana on 14/02/25.
 */

@Singleton
class RemoteDataSource @Inject constructor(
    private val safeApiCall: SafeApiCall,
    private val apiService: GithubApiService,
) {

    private val dispatcherIo = Dispatchers.IO

    fun getListUser(): Flow<ApiResponse<MutableList<ItemListUser>>> {
        return flow {
            emit(safeApiCall.execute { apiService.getListUser() })
        }.flowOn(dispatcherIo)
    }

    fun getDetailUser(username: String?): Flow<ApiResponse<DetailUserResponse>> {
        return flow {
            emit(safeApiCall.execute { apiService.getDetailUser(username) })
        }.flowOn(dispatcherIo)
    }

    fun getUserRepos(username: String?): Flow<ApiResponse<MutableList<ItemRepos>>> {
        return flow {
            emit(safeApiCall.execute { apiService.getUserRepos(username) })
        }.flowOn(dispatcherIo)
    }

    suspend fun searchUser(query: String?, page: Int?): ApiResponse<SearchUserResponse> {
        return safeApiCall.execute { apiService.searchUser(query, page) }
    }
}