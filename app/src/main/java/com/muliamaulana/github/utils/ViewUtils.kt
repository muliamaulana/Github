package com.muliamaulana.github.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muliamaulana.github.R

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