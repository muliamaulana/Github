package com.muliamaulana.github.domain.usecase

import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import kotlinx.coroutines.flow.Flow

/**
 * Created by muliamaulana on 14/02/25.
 */

interface GithubUseCase {
    fun getListUser(): Flow<Resource<MutableList<ItemListUser>>>
}