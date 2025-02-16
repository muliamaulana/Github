package com.muliamaulana.github.features.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.muliamaulana.github.R
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.databinding.ActivityMainBinding
import com.muliamaulana.github.databinding.ItemListUserBinding
import com.muliamaulana.github.features.detailuser.DetailUserActivity
import com.muliamaulana.github.features.search.SearchFragment
import com.muliamaulana.github.utils.CoreAdapter
import com.muliamaulana.github.utils.loadImageProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { CoreAdapter<ItemListUser>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpSearch()
        setUpObserver()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.hasFocus()) {
                    closeSearch()
                    return
                }
                
                if (binding.rvUser.isVisible) {
                    finish()
                    return
                }

                if (!binding.searchView.hasFocus()) {
                    binding.searchView.apply {
                        setQuery("", false)
                    }
                    closeSearch()
                    return
                }

                finish()
            }
        })

        if (viewModel.listUser.value.isNullOrEmpty()) {
            fetchData()
            return
        }

        updateUI()

    }

    private fun setUpSearch() {
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.updateQuerySearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.updateQuerySearch(newText)
                    return true
                }
            })

            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus || binding.searchView.query.isNotEmpty()) {
                    openSearch()
                } else closeSearch()
            }
        }
    }

    private fun fetchData() {
        viewModel.getListUser()
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            viewModel.fetchData.collectLatest { response ->
                when (response) {
                    is Resource.Error -> showError(response.message)
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> updateUI()
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
        binding.layoutError.root.isVisible = false
        binding.progressBar.isVisible = loading
    }

    private fun setUpAdapter() {
        adapter.createViewHolder = { viewGroup ->
            ItemListUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        adapter.viewHolderBinding = { dataItem, viewBinding ->
            val itemBinding = viewBinding as ItemListUserBinding
            itemBinding.apply {
                tvUserName.text = dataItem?.login
                imgProfile.loadImageProfile(dataItem?.avatarUrl)
                root.setOnClickListener {
                    val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.USERNAME, dataItem?.login)
                    startActivity(intent)
                }
            }
        }

    }

    private fun updateUI() {
        showLoading(false)
        setUpAdapter()
        val data = viewModel.listUser.value
        adapter.submitData(data)
        with(binding) {
            rvUser.isVisible = true
            progressBar.isVisible = false
            rvUser.adapter = adapter
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

    private fun openSearch() {
        binding.rvUser.isVisible = false
        binding.layoutError.root.isVisible = false
        binding.progressBar.isVisible = false

        val fragmentManager = supportFragmentManager
        val existingFragment = fragmentManager.findFragmentById(R.id.fragment_container)

        if (existingFragment == null) { // Check if fragment is already present
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .commit()
        }
    }

    private fun closeSearch() {
        supportFragmentManager.findFragmentById(R.id.fragment_container)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
        fetchData()
    }

}