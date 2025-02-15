package com.muliamaulana.github.data.source.remote.network

import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by muliamaulana on 14/02/25.
 */

interface GithubApiService {

    @GET("users")
    suspend fun getListUser(): Response<MutableList<ItemListUser>>

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String?
    ): Response<DetailUserResponse>

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String?
    ): Response<MutableList<ItemRepos>>
}