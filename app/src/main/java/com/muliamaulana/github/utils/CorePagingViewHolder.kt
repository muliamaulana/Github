package com.muliamaulana.github.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by muliamaulana on 15/02/25.
 */

class CorePagingViewHolder <T> internal constructor(
    private val binding: ViewBinding,
    private val expression: ((T?, ViewBinding) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T?) {
        expression(item, binding)
    }
}