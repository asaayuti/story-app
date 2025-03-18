package com.example.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.api.response.LoginResult

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> = repository.getSession().asLiveData()

    fun getStoriesWithLocation(token: String) = repository.getStoriesWithLocation(token)


}