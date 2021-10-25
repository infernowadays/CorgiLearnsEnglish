package com.lazysecs.corgilearnsenglish.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lazysecs.corgilearnsenglish.databinding.SearchItemBinding
import com.lazysecs.domain.models.microsoft.dictionary_lookup.DictionaryLookupTranslation

class SearchAdapter : ListAdapter<DictionaryLookupTranslation, SearchVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchVH(
        SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DictionaryLookupTranslation>() {
            override fun areItemsTheSame(
                oldItem: DictionaryLookupTranslation,
                newItem: DictionaryLookupTranslation
            ): Boolean {
                return oldItem.displayTarget == newItem.displayTarget
            }

            override fun areContentsTheSame(
                oldItem: DictionaryLookupTranslation,
                newItem: DictionaryLookupTranslation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}