package com.muliamaulana.github

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muliamaulana.github.data.Resource
import com.muliamaulana.github.data.source.remote.response.ItemListUser
import com.muliamaulana.github.domain.usecase.GithubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by muliamaulana on 14/02/25.
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: GithubUseCase
) : ViewModel() {

    private val _listUser = MutableStateFlow<MutableList<ItemListUser>?>(null)
    val listUser = _listUser.asStateFlow()
    fun updateListUser(list: MutableList<ItemListUser>?) {
        _listUser.value = list
    }


    private val _fetchData =
        MutableStateFlow<Resource<MutableList<ItemListUser>>>(Resource.Loading())
    val fetchData = _fetchData.asStateFlow()
    fun getListUser() = viewModelScope.launch {
        val result = useCase.getListUser()
        result.collectLatest {
            val data = it.data
            if (!data.isNullOrEmpty()) updateListUser(data)
            _fetchData.value = it
        }
    }
}