package com.muliamaulana.github.data.source.remote

import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.network.GithubApiService
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
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
    private val exceptionHandler: ExceptionHandler,
    private val apiService: GithubApiService,
) {

    private val dispatcherIo = Dispatchers.IO

    fun getListUser(): Flow<ApiResponse<MutableList<ItemListUser>>> {
        return flow {
            try {
                val response = apiService.getListUser()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        emit(ApiResponse.Success(body))
                        return@flow
                    }
                    emit(ApiResponse.Empty)
                    return@flow
                }
                emit(ApiResponse.Empty)
                return@flow
            } catch (e: Exception) {
                emit(ApiResponse.Error(exceptionHandler.handler(e)))
            }
        }.flowOn(dispatcherIo)
    }

    fun getDetailUser(username: String?): Flow<ApiResponse<DetailUserResponse>> {
        return flow {
            try {
                val response = apiService.getDetailUser(username)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        emit(ApiResponse.Success(body))
                        return@flow
                    }
                    emit(ApiResponse.Empty)
                    return@flow
                }
                emit(ApiResponse.Empty)
                return@flow
            } catch (e: Exception) {
                emit(ApiResponse.Error(exceptionHandler.handler(e)))
            }
        }.flowOn(dispatcherIo)
    }

    fun getUserRepos(username: String?): Flow<ApiResponse<MutableList<ItemRepos>>> {
        return flow {
            try {
                val response = apiService.getUserRepos(username)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        emit(ApiResponse.Success(body))
                        return@flow
                    }
                }
                emit(ApiResponse.Empty)
                return@flow
            } catch (e: Exception) {
                emit(ApiResponse.Error(exceptionHandler.handler(e)))
            }
        }.flowOn(dispatcherIo)
    }
}