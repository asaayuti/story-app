package com.example.storyapp.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.data.api.response.StoryItem
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.view.adapter.StoryListAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(StoryListAdapter.KEY_STORY, StoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(StoryListAdapter.KEY_STORY)
        }

        binding.apply {
            tvDetailName.text = story?.name
            tvDetailDescription.text = story?.description
            Glide.with(binding.ivDetailPhoto.context)
                .load(story?.photoUrl)
                .into(binding.ivDetailPhoto)
        }


    }
}