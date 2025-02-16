package com.muliamaulana.github.domain.usecase

import androidx.paging.PagingData
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import kotlinx.coroutines.flow.Flow

/**
 * Created by muliamaulana on 14/02/25.
 */

interface GithubUseCase {
    fun getListUser(): Flow<Resource<MutableList<ItemListUser>>>
    fun getDetailUser(username: String?): Flow<Resource<DetailUserResponse>>
    fun getRepos(username: String?): Flow<Resource<MutableList<ItemRepos>>>
    fun searchUser(query: String?): Flow<PagingData<ItemListUser>>

}