package com.example.dicegame

import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

open class Player(diceValues: Array<Int>) : Serializable {
    private var dieValues = diceValues
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
    fun throwDice() {
        for (die in this.dice) {
            die.rollDie()
        }
    }

    fun getDiceImageResource(number: Int): Int {
        return when (number) {
            1 -> R.drawable.die_1
            2 -> R.drawable.die_2
            3 -> R.drawable.die_3
            4 -> R.drawable.die_4
            5 -> R.drawable.die_5
            else -> R.drawable.die_6
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

    fun getDieButtonArray(): Array<Int> {
        return dieValues
    }
    fun totalScore(): Int {
        var total = 0
        for (die in this.dice) {
            total += die.getDieValue()
        }
        return total
    }

    fun resetDice() {
        for (i in 1..6){
            dice[i-1].setDieValue(i)
        }
    }

    fun resetDiceEnabled() {
        for (die in this.dice) {
            die.setDieEnabled(true)
        }
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
                1 -> R.drawable.die_1
                2 -> R.drawable.die_2
                3 -> R.drawable.die_3
                4 -> R.drawable.die_4
                5 -> R.drawable.die_5
                else -> R.drawable.die_6
            }
        }
    }
}