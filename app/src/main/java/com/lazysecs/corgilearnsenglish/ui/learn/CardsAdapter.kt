package com.lazysecs.corgilearnsenglish.ui.learn

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lazysecs.corgilearnsenglish.databinding.WordCardItemBinding

class CardsAdapter(
    private val nextWordCallback: NextWordCallback,
    private val context: Context,
) : ListAdapter<WordCard, WordCardVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordCardVH(
        WordCardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        nextWordCallback = nextWordCallback,
        context = context,
    )

    override fun onBindViewHolder(holder: WordCardVH, position: Int) {
        holder.bind(getItem(position), itemCount - 1 == position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordCard>() {
            override fun areItemsTheSame(
                oldItem: WordCard,
                newItem: WordCard
            ): Boolean {
                return oldItem.word == newItem.word
            }

            override fun areContentsTheSame(
                oldItem: WordCard,
                newItem: WordCard
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}