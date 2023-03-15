package com.example.dicegame

import android.os.Parcelable
import android.widget.ImageButton
import java.io.Serializable

open class Player(diceBtns: Array<ImageButton>) : Serializable {
    private var dieBtns = diceBtns
    private val dice: Array<Die> = arrayOf(Die(diceBtns[0]), Die(diceBtns[1]), Die(diceBtns[2]), Die(diceBtns[3]), Die(diceBtns[4]))

    fun lockDiceCount(): Int {
        var count = 0
        for (die in this.dice) {
            if (!die.isDieEnabled()) {
                count++
            }
        }
        return count
    }
    fun getDice(): Array<Die> {
        return dice
    }
    fun throwDice() {
        for (die in this.dice) {
            die.rollDie()
        }
    }

    fun reDrawDie(index: Int,value: Int) {
        dice[index].setDieImage(value)
    }

    fun getDieButtonArray(): Array<ImageButton> {
        return dieBtns
    }
    fun totalScore(): Int {
        var total = 0
        for (die in this.dice) {
            total += die.getDieValue()
        }
        return total
    }

    fun resetDice() {
        dice.forEachIndexed() { index, die ->
            if (!die.isDieEnabled())
                die.dieEnableToggle()
            die.setDieValue(index+1)
        }
    }
}