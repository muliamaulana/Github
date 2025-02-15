package com.muliamaulana.github.features.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.muliamaulana.github.R
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import com.muliamaulana.github.databinding.ActivityDetailUserBinding
import com.muliamaulana.github.databinding.ItemListReposBinding
import com.muliamaulana.github.utils.CoreAdapter
import com.muliamaulana.github.utils.loadImage
import com.muliamaulana.github.utils.loadImageProfile
import com.muliamaulana.github.utils.toRelativeTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel: DetailUserViemodel by viewModels()
    private val adapter by lazy { CoreAdapter<ItemRepos>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setUsername(intent?.getStringExtra(USERNAME))

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = viewModel.username.value

        setUpObserver()

        if (viewModel.dataUser.value == null) {
            fetchData()
            return
        }
    }

    private fun fetchData() {
        viewModel.getDetailUser()
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            viewModel.fetchData.collectLatest { response ->
                when (response) {
                    is Resource.Error -> showError(response.message)
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> updateUiUser()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.fetchRepos.collectLatest { response ->
                when (response) {
                    is Resource.Error -> showErrorRepos(response.message)
                    is Resource.Loading -> showLoadingRepos(true)
                    is Resource.Success -> updateUiRepos()
                }
            }
        }
    }

    private fun showError(message: String?) {
        showLoading(false)
        binding.layoutError.apply {
            root.isVisible = true
            errorMessage.text = message
            retryProgress.setOnClickListener {
                fetchData()
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.appBar.isVisible = false
        binding.layoutError.root.isVisible = false
        binding.layoutContent.root.isVisible = false
        binding.progressBar.isVisible = loading

    }

    private fun updateUiUser() {
        viewModel.getUserRepos()
        showLoading(false)
        binding.appBar.isVisible = true
        binding.layoutContent.root.isVisible = true
        val data = viewModel.dataUser.value

        with(binding) {
            ivHeader.loadImage(data?.avatarUrl)
        }

        with(binding.layoutContent) {

            ivProfileImage.loadImageProfile(data?.avatarUrl)
            tvLocation.isVisible = !data?.location.isNullOrEmpty()
            tvLocation.text = data?.location

            tvBlog.isVisible = !data?.blog.isNullOrEmpty()
            tvBlog.text = data?.blog

            tvEmail.isVisible = !data?.email.isNullOrEmpty()
            tvEmail.text = data?.email

            tvFollowersCount.text = data?.followers.toString()
            tvFollowingCount.text = data?.following.toString()
            tvRepositoryCount.text = data?.publicRepos.toString()

        }
    }

    private fun showErrorRepos(message: String?) {
        showLoadingRepos(false)
        binding.layoutContent.layoutErrorRepos.apply {
            root.isVisible = true
            errorMessage.text = message
            retryProgress.setOnClickListener {
                fetchData()
            }
        }
    }

    private fun showLoadingRepos(loading: Boolean) {
        with(binding.layoutContent) {
            layoutErrorRepos.root.isVisible = false
            rvRepos.isVisible = false
            progressBarRepos.isVisible = loading
        }

    }

    private fun updateUiRepos() {
        showLoadingRepos(false)
        setUpAdapter()
        binding.layoutContent.root.isVisible = true
        val data = viewModel.dataRepos.value

        adapter.submitData(data)
        with(binding.layoutContent.rvRepos) {
            isVisible = true
            adapter = this@DetailUserActivity.adapter
            layoutManager = LinearLayoutManager(this@DetailUserActivity)
        }
    }

    private fun setUpAdapter() {
        adapter.createViewHolder = { viewGroup ->
            ItemListReposBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        adapter.viewHolderBinding = { dataItem, viewBinding ->
            val itemBinding = viewBinding as ItemListReposBinding
            itemBinding.apply {
                tvRepoName.text = dataItem?.name ?: "-"
                tvRepoDesc.text = dataItem?.description ?: "-"

                chipProgramming.isVisible = !dataItem?.language.isNullOrEmpty()
                chipProgramming.text = dataItem?.language
                tvUpdated.text = dataItem?.updatedAt?.toRelativeTime()
            }
        }
    }


    companion object {
        const val USERNAME = "username"
    }
}