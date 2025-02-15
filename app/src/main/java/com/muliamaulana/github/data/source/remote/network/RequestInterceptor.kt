package com.muliamaulana.github.data.source.remote.network

import com.muliamaulana.github.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by muliamaulana on 15/02/25.
 */

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val token = BuildConfig.GITHUB_TOKEN

        val url = originalHttpUrl.newBuilder().build()

        // Request customization: add request headers
        val requestBuilder: Request.Builder = original.newBuilder()
        requestBuilder.header("Authorization", "Bearer $token")
        requestBuilder.url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}