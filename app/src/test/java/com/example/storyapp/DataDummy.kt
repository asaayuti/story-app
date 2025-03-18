package com.example.storyapp

import com.example.storyapp.data.api.response.StoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<StoryItem> {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryItem(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
                photoUrl = "photo $i"
            )
            items.add(quote)
        }
        return items
    }
}