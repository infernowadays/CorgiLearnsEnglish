package com.lazysecs.corgilearnsenglish.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.databinding.SearchItemBinding
import com.lazysecs.domain.models.microsoft.dictionary_lookup.DictionaryLookupTranslation

class SearchVH(
    private val binding: SearchItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dictionaryLookupTranslation: DictionaryLookupTranslation) {
        binding.tvDisplayTarget.text = dictionaryLookupTranslation.displayTarget
        binding.tvBackTranslations.text = dictionaryLookupTranslation.backTranslations.joinToString { it.displayText }
    }
}