package com.example.dicegame

import android.widget.ImageButton

class HumanDice(diceBtns: Array<ImageButton>) {
    private val dice: Array<Die> = arrayOf(Die(diceBtns[0]), Die(diceBtns[1]), Die(diceBtns[2]), Die(diceBtns[3]), Die(diceBtns[4]))

    fun throwDice() {
        for (die in this.dice) {
            die.rollDie()
        }
    }


}