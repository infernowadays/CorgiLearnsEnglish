package com.lazysecs.corgilearnsenglish.ui.word_list

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lazysecs.corgilearnsenglish.databinding.WordListItemBinding
import com.lazysecs.domain.models.WordList

class WordListsAdapter(
    private val wordListClickListener: WordListClickListener,
    private val resources: Resources,
) : ListAdapter<WordList, WordListVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListVH {
        return WordListVH(
            WordListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            wordListClickListener = wordListClickListener,
            resources = resources
        )
    }

    override fun onBindViewHolder(holder: WordListVH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordList>() {
            override fun areItemsTheSame(
                oldItem: WordList,
                newItem: WordList
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: WordList,
                newItem: WordList
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}