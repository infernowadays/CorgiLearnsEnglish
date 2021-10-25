package com.lazysecs.corgilearnsenglish.ui.select_language

import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.databinding.LanguageItemBinding
import com.lazysecs.domain.models.Language

class LanguageVH(
    private val binding: LanguageItemBinding,
    private val selectLanguage: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(language: Language, selected: Boolean) {
        binding.tvLanguage.text = language.name
        binding.ivSelected.isVisible = selected
        binding.root.setOnClickListener {
            selectLanguage(language.name)
            binding.ivSelected.visibility = VISIBLE
        }
    }
}