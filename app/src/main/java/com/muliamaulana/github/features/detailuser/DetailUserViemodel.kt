package com.muliamaulana.github.features.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.DetailUserResponse
import com.muliamaulana.github.data.source.remote.response.ItemRepos
import com.muliamaulana.github.domain.usecase.GithubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by muliamaulana on 15/02/25.
 */

@HiltViewModel
class DetailUserViemodel @Inject constructor(
    private val useCase: GithubUseCase
) : ViewModel() {

    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()
    fun setUsername(username: String?) {
        _username.value = username
    }

    private val _dataUser = MutableStateFlow<DetailUserResponse?>(null)
    val dataUser = _dataUser.asStateFlow()
    fun updateDataUser(data: DetailUserResponse?) {
        _dataUser.value = data
    }

    private val _fetchData =
        MutableStateFlow<Resource<DetailUserResponse>>(Resource.Loading())
    val fetchData = _fetchData.asStateFlow()
    fun getDetailUser() = viewModelScope.launch {
        val result = useCase.getDetailUser(_username.value)
        result.collectLatest {
            val data = it.data
            if (data != null) updateDataUser(data)
            _fetchData.value = it
        }
    }

    private val _dataRepos = MutableStateFlow<MutableList<ItemRepos>?>(null)
    val dataRepos = _dataRepos.asStateFlow()
    fun updateDataRepos(data: MutableList<ItemRepos>?) {
        _dataRepos.value = data
    }

    private val _fetchRepos = MutableStateFlow<Resource<MutableList<ItemRepos>>>(Resource.Loading())
    val fetchRepos = _fetchRepos.asStateFlow()
    fun getUserRepos() = viewModelScope.launch {
        val result = useCase.getRepos(_username.value)
        result.collectLatest {
            val data = it.data
            if (data != null) updateDataRepos(data)
            _fetchRepos.value = it
        }
    }

}