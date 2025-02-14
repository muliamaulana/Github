package com.muliamaulana.github.data.source.remote.network

import com.muliamaulana.github.data.source.remote.response.ItemListUser
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by muliamaulana on 14/02/25.
 */

interface GithubApiService {

    @GET("users")
    suspend fun getListUser(): Response<MutableList<ItemListUser>>
}