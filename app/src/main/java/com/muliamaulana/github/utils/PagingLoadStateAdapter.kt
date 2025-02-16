package com.muliamaulana.dicodingmade.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muliamaulana.github.databinding.ItemPagingLoadStateFooterBinding

/**
 * Created by muliamaulana on 15/02/25.
 */

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadstate: LoadState): LoadStateViewHolder {
        val binding =
            ItemPagingLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(
        private val binding: ItemPagingLoadStateFooterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetryFooter.setOnClickListener {
                retry.invoke()
            }

        }

        fun bind(loadstate: LoadState) {
            binding.apply {
                pbFooter.isVisible = loadstate is LoadState.Loading
                btnRetryFooter.isVisible = loadstate !is LoadState.Loading
                tvErrorFooter.isVisible = loadstate !is LoadState.Loading
                if (loadstate is LoadState.Error) {
                    tvErrorFooter.text = loadstate.error.message
                }
            }
        }


    }
}