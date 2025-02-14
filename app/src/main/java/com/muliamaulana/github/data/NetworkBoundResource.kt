package com.muliamaulana.github.data

import com.muliamaulana.github.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/**
 * Created by muliamaulana on 14/02/25.
 */

abstract class NetworkBoundResource<RESPONSE> {

    private val result: Flow<Resource<RESPONSE>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
            is ApiResponse.Empty -> emit(Resource.Success(null))
            is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
        }

    }

    protected abstract suspend fun createCall(): Flow<ApiResponse<RESPONSE>>

    fun asFlow(): Flow<Resource<RESPONSE>> = result

    protected open fun onFetchFailed() = Unit
}