package com.muliamaulana.github.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by muliamaulana on 15/02/25.
 */

class CoreViewHolder<T> internal constructor(
    private val binding: ViewBinding,
    private val expression: ((T?, ViewBinding) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T?) {
        expression?.invoke(item, binding)
    }
}