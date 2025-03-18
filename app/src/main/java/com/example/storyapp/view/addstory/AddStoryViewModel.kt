package com.example.storyapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.api.response.LoginResult
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun uploadStoryWithLocation(
        token: String,
        imageFile: File,
        description: String,
        lat: Float,
        lon: Float
    ) =
        repository.uploadStoryWithLocation(token, imageFile, description, lat, lon)

    fun getSession(): LiveData<LoginResult> = repository.getSession().asLiveData()

}