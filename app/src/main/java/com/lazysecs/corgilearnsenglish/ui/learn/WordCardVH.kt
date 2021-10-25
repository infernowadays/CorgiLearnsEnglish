package com.lazysecs.corgilearnsenglish.ui.learn

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.WordCardItemBinding

class WordCardVH(
    private val binding: WordCardItemBinding,
    private val nextWordCallback: NextWordCallback,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wordCard: WordCard, lastCard: Boolean) {
        binding.tvWord.text = wordCard.word
        binding.rbFirstOption.text = wordCard.options[0]
        binding.rbSecondOption.text = wordCard.options[1]
        binding.rbThirdOption.text = wordCard.options[2]
        binding.rbFourthOption.text = wordCard.options[3]
        binding.rbFifthOption.text = wordCard.options[4]
        binding.btnNext.text = if (lastCard) {
            context.resources.getString(R.string.finish)
        } else {
            context.getString(R.string.next_word)
        }
        binding.btnNext.setOnClickListener {
            nextWordCallback.next(lastCard)
            binding.btnNext.isEnabled = !lastCard
        }
        setAnswerViewAttrs(wordCard.answer)
    }

    private fun disableOptions() {
        for (i in 0 until binding.rgOptions.childCount) {
            binding.rgOptions.getChildAt(i).isClickable = false
        }
    }

    private fun setAnswerViewAttrs(answer: String) {
        for (i in 0 until binding.rgOptions.childCount) {
            val option = binding.rgOptions.getChildAt(i) as RadioButton
            if (option.text == answer) {
                option.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        option.setColor(ContextCompat.getColor(context, R.color.green))
                        disableOptions()
                        binding.btnNext.isEnabled = true
                        binding.rgOptions.setOnCheckedChangeListener(null)
                    }
                }
            }
        }
    }

    private fun RadioButton.setColor(color: Int) {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                Color.GRAY,
                color
            )
        )
        buttonTintList = colorStateList
        backgroundTintList = colorStateList
        invalidate()
    }
}