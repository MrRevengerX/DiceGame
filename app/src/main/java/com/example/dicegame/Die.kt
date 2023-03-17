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

    fun getDieValue(): Int {
        return dieValue
    }

    fun isDieEnabled(): Boolean {
        return enabled
    }

    fun setDieEnabled(value: Boolean) {
        enabled = value
    }



}
