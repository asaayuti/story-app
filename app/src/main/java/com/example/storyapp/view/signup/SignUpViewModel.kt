package com.example.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository

class SignUpViewModel(private val repository: StoryRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}