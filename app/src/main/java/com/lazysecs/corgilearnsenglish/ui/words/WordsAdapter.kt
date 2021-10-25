package com.lazysecs.corgilearnsenglish.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.lazysecs.corgilearnsenglish.databinding.WordItemBinding
import com.lazysecs.domain.models.Word

class WordsAdapter(
    private val deleteCallback: (String) -> Unit,
    private val clickCallback: (String, String) -> Unit
) : ListAdapter<Word, WordVH>(DIFF_CALLBACK) {

    private val viewBinderHelper = ViewBinderHelper()
    private var lastSwipedWord: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordVH(
        WordItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        swipeOpenedCallback = { lastSwipedWord = it },
        clickCallback = { text, languageCode ->
            if (lastSwipedWord != null) viewBinderHelper.closeLayout(lastSwipedWord)
            clickCallback.invoke(text, languageCode)
        },
        deleteCallback = deleteCallback,
    )

    override fun onBindViewHolder(holder: WordVH, position: Int) {
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(holder.binding.swipe, getItem(position).text)
        holder.bind(getItem(position))
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(
                oldItem: Word,
                newItem: Word
            ): Boolean {
                return oldItem.text == newItem.text
            }

            override fun areContentsTheSame(
                oldItem: Word,
                newItem: Word
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}