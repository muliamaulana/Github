package com.muliamaulana.github.data.source.remote.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muliamaulana.github.data.source.remote.RemoteDataSource
import com.muliamaulana.github.data.source.remote.network.ApiResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import javax.inject.Inject

/**
 * Created by muliamaulana on 15/02/25.
 */

const val STARTING_PAGE_INDEX = 1

class SearchPagingSource @Inject constructor(
    private val remoteDataource: RemoteDataSource,
    private val query: String?
) : PagingSource<Int, ItemListUser>() {

    override fun getRefreshKey(state: PagingState<Int, ItemListUser>): Int {
        return STARTING_PAGE_INDEX
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemListUser> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return when (val response = remoteDataource.searchUser(query = query, page = position)) {
            is ApiResponse.Success -> {
                val data = response.data.items
                LoadResult.Page(
                    data = data ?: emptyList(),
                    prevKey = null,
                    nextKey = if (data.isNullOrEmpty()) null else position + 1
                )
            }

            is ApiResponse.Empty -> {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            is ApiResponse.Error -> {
                LoadResult.Error(Throwable(response.errorMessage))
            }
        }
    }
}