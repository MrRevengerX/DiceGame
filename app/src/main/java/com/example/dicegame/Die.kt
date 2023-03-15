package com.example.dicegame

import android.widget.ImageButton
import android.content.res.Resources
import android.content.Context

class Die(DieImage: ImageButton) {
    private var dieValue: Int = 0
    private var enabled: Boolean = true
    private var die: ImageButton = DieImage
    private val dieImages: Array<Int> = arrayOf(R.drawable.die_1, R.drawable.die_2, R.drawable.die_3, R.drawable.die_4, R.drawable.die_5, R.drawable.die_6)
    private val dieImagesLocked: Array<Int> = arrayOf(R.drawable.die_1_locked, R.drawable.die_2_locked, R.drawable.die_3_locked, R.drawable.die_4_locked, R.drawable.die_5_locked, R.drawable.die_6_locked)


    fun rollDie() {
        if (enabled) {
            dieValue = (1..6).random()
            die.setImageResource(dieImages[dieValue - 1])
        }
    }

    fun setDieValue(value: Int) {
        dieValue = value
        die.setImageResource(dieImages[dieValue - 1])
    }

    fun isDieEnabled(): Boolean {
        return enabled
    }

    fun getDieValue(): Int {
        return dieValue
    }

    fun dieEnableToggle() {
//        change die image to disable state
        if (enabled) {
            die.setImageResource(dieImagesLocked[dieValue - 1])
        } else {
            die.setImageResource(dieImages[dieValue - 1])
        }
        enabled = !enabled
    }
}
