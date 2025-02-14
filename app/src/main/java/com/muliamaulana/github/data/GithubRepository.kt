package com.muliamaulana.github.data

import com.muliamaulana.github.data.source.remote.RemoteDataSource
import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.domain.repository.IGithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by muliamaulana on 14/02/25.
 */

@Singleton
class GithubRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : IGithubRepository {

    override fun getListUser(): Flow<Resource<MutableList<ItemListUser>>> {
        return object : NetworkBoundResource<MutableList<ItemListUser>>() {
            override suspend fun createCall(): Flow<ApiResponse<MutableList<ItemListUser>>> {
                return remoteDataSource.getListUser()
            }
        }.asFlow()
    }
}