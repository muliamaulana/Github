package com.muliamaulana.github.utils

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by muliamaulana on 15/02/25.
 */

class CoreAdapter<T : Any> : RecyclerView.Adapter<CoreViewHolder<T>>() {

    @SuppressLint("DiffUtilEquals")
    private val differCallBack = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: MutableList<T>?) {
        differ.submitList(data)
        notifyDataSetChanged()
    }

    fun getExistItem(): MutableList<T> {
        if (itemCount > 0) {
            return differ.currentList
        }
        return mutableListOf()
    }

    lateinit var createViewHolder: ((ViewGroup) -> ViewBinding)
    var viewHolderBinding: ((T?, ViewBinding) -> Unit)? = null


    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreViewHolder<T> {
        return CoreViewHolder(createViewHolder(parent), viewHolderBinding)
    }

    override fun onBindViewHolder(holder: CoreViewHolder<T>, position: Int) {
        holder.bind(differ.currentList[position])
    }

}
