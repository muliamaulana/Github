package com.muliamaulana.github.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.muliamaulana.github.data.source.remote.RemoteDataSource
import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.pagingsource.SearchPagingSource
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import com.muliamaulana.github.domain.repository.IGithubRepository
import com.muliamaulana.github.utils.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by muliamaulana on 14/02/25.
 */

@Singleton
class GithubRepository @Inject constructor(
    private val exceptionHandler: ExceptionHandler,
    private val remoteDataSource: RemoteDataSource
) : IGithubRepository {

    override fun getListUser(): Flow<Resource<MutableList<ItemListUser>>> {
        return object : NetworkBoundResource<MutableList<ItemListUser>>() {
            override suspend fun createCall(): Flow<ApiResponse<MutableList<ItemListUser>>> {
                return remoteDataSource.getListUser()
            }
        }.asFlow()
    }

    override fun getDetailUser(username: String?): Flow<Resource<DetailUserResponse>> {
        return object : NetworkBoundResource<DetailUserResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<DetailUserResponse>> {
                return remoteDataSource.getDetailUser(username)
            }
        }.asFlow()
    }

    override fun getUserRepos(username: String?): Flow<Resource<MutableList<ItemRepos>>> {
        return object : NetworkBoundResource<MutableList<ItemRepos>>() {
            override suspend fun createCall(): Flow<ApiResponse<MutableList<ItemRepos>>> {
                return remoteDataSource.getUserRepos(username)
            }
        }.asFlow()
    }

    override fun searchUser(query: String?): Flow<PagingData<ItemListUser>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                SearchPagingSource(
                    remoteDataSource,
                    query = query
                )
            }
        ).flow.flowOn(Dispatchers.IO)
    }
}