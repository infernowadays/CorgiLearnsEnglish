package com.lazysecs.corgilearnsenglish.utils

import android.graphics.Color
import kotlin.math.abs

open class ColorUtil {

    companion object {
        private val colors = mapOf(
            0 to Color.parseColor("#fbf8cc"),
            1 to Color.parseColor("#fde4cf"),
            2 to Color.parseColor("#ffcfd2"),
            3 to Color.parseColor("#f1c0e8"),
            4 to Color.parseColor("#cfbaf0"),
            5 to Color.parseColor("#a3c4f3"),
            6 to Color.parseColor("#90dbf4"),
            7 to Color.parseColor("#8eecf5"),
            8 to Color.parseColor("#98f5e1"),
            9 to Color.parseColor("#b9fbc0"),
        )

        fun getColor(hash: Int): Int {
            return colors[abs(hash % colors.size)] ?: Color.parseColor("#fbf8cc")
        }
    }
}