package com.example.dicegame

import android.widget.ImageButton
import android.content.res.Resources
import android.content.Context

class Die(value:Int): java.io.Serializable {
    private var dieValue: Int = value
    private var enabled: Boolean = true

    fun rollDie() {
        if (enabled) {
            dieValue = (1..6).random()
        }
    }

    fun setDieValue(value: Int) {
        dieValue = value
    }

//    fun getDieImage(value: Int): Int {
//        return dieImages[value]
//    }
//
//    fun setDieImage(num:Int) {
//        die.setImageResource(dieImages[num-1])
//    }

    fun isDieEnabled(): Boolean {
        return enabled
    }

    fun setDieEnabled(value: Boolean) {
        enabled = value
    }

    fun getDieValue(): Int {
        return dieValue
    }


}
