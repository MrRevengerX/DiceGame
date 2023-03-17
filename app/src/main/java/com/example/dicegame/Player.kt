package com.example.dicegame

import java.io.Serializable

open class Player(diceValues: Array<Int>) : Serializable {
    private val dice: Array<Die> = arrayOf(Die(diceValues[0]), Die(diceValues[1]), Die(diceValues[2]), Die(diceValues[3]), Die(diceValues[4]))


    fun getDice(): Array<Die> {
        return dice
    }

//    Function to get the value of the dice
    fun getDieValueArray(): Array<Int> {
        val dieValueArray = arrayOf(0, 0, 0, 0, 0)
        for (i in 0..4) {
            dieValueArray[i] = dice[i].getDieValue()
        }
        return dieValueArray
    }

//    Function to get total locked elements in the dice
    fun getLockDiceCount(): Int {
        var count = 0
        for (die in this.dice) {
            if (!die.isDieEnabled()) {
                count++
            }
        }
        return count
    }

//    Function to get the image resource of the dice
//    Reference : https://kotlinlang.org/docs/control-flow.html#when-expression
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

//    Function to get the image resource of the locked dice
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

//    Function to roll dice
    fun throwDice() {
        for (die in this.dice) {
            die.rollDie()
        }
    }

//    Function to reset the dice to enabled state
    fun resetDiceLock() {
        for (die in this.dice) {
            die.setDieEnabled(true)
        }
    }


//    Function to calculate the total score of the dice
    fun totalScore(): Int {
        var total = 0
        for (die in this.dice) {
            total += die.getDieValue()
        }
        return total
    }

//    Function to get image resource for die according to its locked or unlocked state
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