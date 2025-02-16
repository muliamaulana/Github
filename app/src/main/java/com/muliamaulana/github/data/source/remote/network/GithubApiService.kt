package com.muliamaulana.github.data.source.remote.network

import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import com.muliamaulana.github.data.source.remote.response.SearchUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String?,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int = 10
    ): Response<SearchUserResponse>

}