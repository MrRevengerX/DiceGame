package com.example.dicegame

import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

open class Player(diceValues: Array<Int>) : Serializable {
    private val dice: Array<Die> = arrayOf(Die(diceValues[0]), Die(diceValues[1]), Die(diceValues[2]), Die(diceValues[3]), Die(diceValues[4]))

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

    fun getDieValueArray(): Array<Int> {
        val dieValueArray = arrayOf(0, 0, 0, 0, 0)
        for (i in 0..4) {
            dieValueArray[i] = dice[i].getDieValue()
        }
        return dieValueArray
    }

    fun throwDice() {
        for (die in this.dice) {
            die.rollDie()
        }
    }

    fun getDiceImageResource(number: Int): Int {
        return when (number) {
            1 -> R.drawable.die_1_img
            2 -> R.drawable.die_2_img
            3 -> R.drawable.die_3_img
            4 -> R.drawable.die_4_img
            5 -> R.drawable.die_5_img
            else -> R.drawable.die_6_img
        }
    }

    fun resetDiceLock() {
        for (die in this.dice) {
            die.setDieEnabled(true)
        }
    }

    fun getLockedDiceImageResource(number: Int): Int {
        return when (number) {
            1 -> R.drawable.die_1_locked
            2 -> R.drawable.die_2_locked
            3 -> R.drawable.die_3_locked
            4 -> R.drawable.die_4_locked
            5 -> R.drawable.die_5_locked
            else -> R.drawable.die_6_locked
        }
    }

//    fun reDrawDie(index: Int,value: Int) {
//        dice[index].setDieImage(value)
//    }

    fun totalScore(): Int {
        var total = 0
        for (die in this.dice) {
            total += die.getDieValue()
        }
        return total
    }


    fun dieEnableToggle(index:Int, value: Int): Int {
//        change die image to disable state
        if (dice[index].isDieEnabled()) {
            return when (value) {
                1 -> R.drawable.die_1_locked
                2 -> R.drawable.die_2_locked
                3 -> R.drawable.die_3_locked
                4 -> R.drawable.die_4_locked
                5 -> R.drawable.die_5_locked
                else -> R.drawable.die_6_locked
            }
        } else {
            return when (value) {
                1 -> R.drawable.die_1_img
                2 -> R.drawable.die_2_img
                3 -> R.drawable.die_3_img
                4 -> R.drawable.die_4_img
                5 -> R.drawable.die_5_img
                else -> R.drawable.die_6_img
            }
        }
    }
}