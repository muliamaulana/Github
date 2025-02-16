package com.muliamaulana.github.domain.repository

import androidx.paging.PagingData
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import kotlinx.coroutines.flow.Flow

/**
 * Created by muliamaulana on 14/02/25.
 */

interface IGithubRepository {
    fun getListUser(): Flow<Resource<MutableList<ItemListUser>>>
    fun getDetailUser(username: String?): Flow<Resource<DetailUserResponse>>
    fun getUserRepos(username: String?): Flow<Resource<MutableList<ItemRepos>>>
    fun searchUser(query: String?): Flow<PagingData<ItemListUser>>
}