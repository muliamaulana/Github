package com.muliamaulana.github.utils

import android.text.format.DateUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muliamaulana.github.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Created by muliamaulana on 15/02/25.
 */

fun ImageView.loadImageProfile(url: String?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_account_circle) // Placeholder image
        .error(R.drawable.ic_error_outline) // Error image
        .apply(RequestOptions().circleCrop()) // Apply rounded transformation
        .into(this)
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_image_placeholder) // Placeholder image
        .error(R.drawable.ic_error_outline) // Error image
        .into(this)
}

fun String.toRelativeTime(): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC") // Set UTC time zone

    return try {
        val date = isoFormat.parse(this) ?: return "-"
        val now = System.currentTimeMillis()
        DateUtils.getRelativeTimeSpanString(
            date.time,
            now,
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    } catch (e: Exception) {
        "-"
    }
}
