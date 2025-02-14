package com.muliamaulana.github.utils

import android.content.Context
import com.chuckerteam.chucker.BuildConfig
import com.muliamaulana.github.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by muliamaulana on 14/02/25.
 */

@Singleton
class ExceptionHandler @Inject constructor(
    private val context: Context
) {

    fun handler(exception: Exception): String {
        return when (exception) {
            is UnknownHostException, is SocketException -> context.getString(R.string.no_connection_message)
            is SocketTimeoutException -> context.getString(R.string.timeout_message)
            is HttpException -> getErrorMessage(exception)
            is IOException -> context.getString(R.string.failed_response_message)
            else -> {
                exception.printStackTrace()
                if (BuildConfig.DEBUG) {
                    exception.message.toString()
                } else context.getString(R.string.something_went_wrong)
            }
        }
    }

    private fun getErrorMessage(exception: HttpException): String {
        return when (exception.code()) {
            401 -> context.getString(R.string.unauthorized)
            404 -> context.getString(R.string.not_found)
            in 500..511 -> context.getString(R.string.server_error_message)
            else -> {
                if (BuildConfig.DEBUG) {
                    "${context.getString(R.string.something_went_wrong)} \n(${exception.message} - ${exception.code()})"
                } else context.getString(R.string.something_went_wrong)
            }
        }
    }
}