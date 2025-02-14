package com.muliamaulana.github.domain.usecase

import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.domain.repository.IGithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by muliamaulana on 14/02/25.
 */

class GithubInteractor @Inject constructor(private val repository: IGithubRepository) :
    GithubUseCase {

    override fun getListUser(): Flow<Resource<MutableList<ItemListUser>>> {
        return repository.getListUser()
    }

}