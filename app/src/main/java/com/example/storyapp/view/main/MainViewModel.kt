package com.example.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.api.response.StoryItem
import com.example.storyapp.data.api.response.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> = repository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }

    fun getStories(token: String): LiveData<PagingData<StoryItem>> =
        repository.getStories(token).cachedIn(viewModelScope)
}