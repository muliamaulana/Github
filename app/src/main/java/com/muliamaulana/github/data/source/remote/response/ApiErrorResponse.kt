package com.muliamaulana.github.data.source.remote.response

import com.google.gson.annotations.SerializedName

/**
 * Created by muliamaulana on 16/02/25.
 */
data class ApiErrorResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("documentation_url")
    val documentationUrl: String?,

    @SerializedName("status")
    val status: Int? = null
)