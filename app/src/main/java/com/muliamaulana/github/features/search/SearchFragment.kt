package com.muliamaulana.github.features.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.muliamaulana.dicodingmade.core.ui.PagingLoadStateAdapter
import com.muliamaulana.github.R
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.databinding.FragmentSearchBinding
import com.muliamaulana.github.databinding.ItemListUserBinding
import com.muliamaulana.github.features.detailuser.DetailUserActivity
import com.muliamaulana.github.features.home.MainViewModel
import com.muliamaulana.github.utils.CorePagingAdapter
import com.muliamaulana.github.utils.loadImageProfile
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val adapterSearch by lazy { CorePagingAdapter<ItemListUser>() }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!! // Non-nullable reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpObserver()
        setUpSearchAdapter()
        return binding.root
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            viewModel.querySearch.collectLatest { query ->
                if (query.isNullOrEmpty()) {
                    resetSearch()
                    return@collectLatest
                }
                viewModel.search(query).collectLatest {
                    adapterSearch.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun resetSearch() {
        binding.layoutErrorSearch.root.isVisible = false
        binding.progressBarSearch.isVisible = false
        adapterSearch.submitData(lifecycle, PagingData.empty())
    }

    private fun setUpSearchAdapter() {
        adapterSearch.createViewHolder = { viewGroup ->
            ItemListUserBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        }

        adapterSearch.viewHolderBinding = { dataItem, viewBinding ->
            val itemBinding = viewBinding as ItemListUserBinding
            itemBinding.apply {
                tvUserName.text = dataItem?.login
                imgProfile.loadImageProfile(dataItem?.avatarUrl)
                root.setOnClickListener {
                    val intent = Intent(requireContext(), DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.USERNAME, dataItem?.login)
                    startActivity(intent)
                }
            }
        }
        val layoutError = binding.layoutErrorSearch

        binding.rvSearch.apply {
            adapter = adapterSearch.withLoadStateFooter(
                footer = PagingLoadStateAdapter { adapterSearch.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launch {
            adapterSearch.loadStateFlow.collectLatest { loadStates ->
                layoutError.root.isVisible = loadStates.source.refresh is LoadState.Error
                binding.progressBarSearch.isVisible = loadStates.source.refresh is LoadState.Loading

                if (loadStates.source.refresh is LoadState.Error) {
                    layoutError.root.isVisible = true
                    binding.rvSearch.isVisible = false
                    layoutError.retryProgress.text = resources.getString(R.string.retry)
                    layoutError.retryProgress.isVisible = true
                    val message = (loadStates.source.refresh as LoadState.Error).error.message
                    layoutError.errorMessage.text = message
                    layoutError.retryProgress.setOnClickListener { adapterSearch.retry() }
                }

                if (loadStates.source.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && adapterSearch.itemCount < 1) {
                    layoutError.root.isVisible = true
                    layoutError.retryProgress.isVisible = false
                    layoutError.errorMessage.text = getString(R.string.no_result)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}