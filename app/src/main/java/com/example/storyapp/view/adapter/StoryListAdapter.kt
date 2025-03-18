package com.example.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.api.response.StoryItem
import com.example.storyapp.databinding.ItemRowStoryBinding
import com.example.storyapp.view.DetailActivity

class StoryListAdapter :
    PagingDataAdapter<StoryItem, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

        holder.binding.apply {
            root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        root.context as Activity,
                        Pair(ivItemPhoto, "photo"),
                        Pair(tvItemName, "name"),
                        Pair(tvItemDescription, "description")
                    )

                Intent(root.context, DetailActivity::class.java).also {
                    it.putExtra(KEY_STORY, story)
                    root.context.startActivity(it, optionsCompat.toBundle())
                }
            }
        }
    }

    class ViewHolder(val binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: StoryItem) {
            binding.tvItemName.text = storyItem.name
            binding.tvItemDescription.text = storyItem.description
            Glide.with(binding.ivItemPhoto.context)
                .load(storyItem.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryItem,
                newItem: StoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }

        const val KEY_STORY = "key_story"
    }
}