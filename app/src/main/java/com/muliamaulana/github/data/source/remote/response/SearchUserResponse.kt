package com.muliamaulana.github.data.source.remote.response

import com.google.gson.annotations.SerializedName

/**
 * Created by muliamaulana on 15/02/25.
 */

data class SearchUserResponse(

    @SerializedName("total_count")
    val totalCount: Int? = null,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @SerializedName("items")
    val items: MutableList<ItemListUser>? = null
)
