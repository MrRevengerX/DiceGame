package com.example.dicegame

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.coroutines.withTimeout

class GameScreen : AppCompatActivity() {

    private var totalHumanScore = 0
    private var totalComputerScore = 0
    private var targetScore = 0
    private var rethrowCount: Int = 0

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        targetScore = intent.getIntExtra("targetScore", 0)

//        Set the target score to the user entered value
        val tvTargetScore = findViewById<TextView>(R.id.tvTargetScore)
        tvTargetScore.text = targetScore.toString()

        gameStart()



    }

    private fun gameStart(){
        val handler = Handler()

//        Declaring buttons and textviews
        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)
        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)

        val btnHumanDie1 = findViewById<ImageButton>(R.id.ibtnHuman1)
        val btnHumanDie2 = findViewById<ImageButton>(R.id.ibtnHuman2)
        val btnHumanDie3 = findViewById<ImageButton>(R.id.ibtnHuman3)
        val btnHumanDie4 = findViewById<ImageButton>(R.id.ibtnHuman4)
        val btnHumanDie5 = findViewById<ImageButton>(R.id.ibtnHuman5)
        val humanDice = arrayOf(btnHumanDie1, btnHumanDie2, btnHumanDie3, btnHumanDie4, btnHumanDie5)

        val btnComputerDie1 = findViewById<ImageButton>(R.id.ibtnComputer1)
        val btnComputerDie2 = findViewById<ImageButton>(R.id.ibtnComputer2)
        val btnComputerDie3 = findViewById<ImageButton>(R.id.ibtnComputer3)
        val btnComputerDie4 = findViewById<ImageButton>(R.id.ibtnComputer4)
        val btnComputerDie5 = findViewById<ImageButton>(R.id.ibtnComputer5)
        val computerDice = arrayOf(btnComputerDie1, btnComputerDie2, btnComputerDie3, btnComputerDie4, btnComputerDie5)

//        Creating objects for the human and computer
        val human = HumanDice(humanDice)
        val computer = ComputerDice(computerDice)


//        Adding click event listener to throw button
        btnThrow.setOnClickListener(){
            rethrowCount++

//            Change the text of the button to "Re-Throw" after the first throw
            if (rethrowCount > 0)
                btnThrow.text = "Re-Throw"

//            Enable the score button after the first throw
            btnScore.isEnabled = true

//            Enable locking dice after the first throw
            humanDice.forEachIndexed() { index, btn ->
                btn.setOnClickListener() {
//                    Block user from locking more than 4 dice
                    if (!human.getDice()[index].isDieEnabled() || human.lockDiceCount() < 4) {
                        human.getDice()[index].dieEnableToggle()
                    }
                    else{
                        Toast.makeText(this, "You can only lock 4 dice", Toast.LENGTH_SHORT).show()
                    }
                }
            }

//            Throw dice for human and computer
            human.throwDice()
            tvHumanScore.text = human.totalScore().toString()
            computer.throwDice()
            tvComputerScore.text = computer.totalScore().toString()

//            If rethrow count is greater than 2, disable the throw button and add score to the total score
            if (rethrowCount > 2) {
                btnThrow.isEnabled = false
                btnScore.isEnabled = false

                Toast.makeText(this, "Out of throws! Score will update soon", Toast.LENGTH_SHORT).show()
//                Reset game after using all throws
                handler.postDelayed({
                    calScore(human,computer)
                    resetGame(human, computer)
                }, 1000)


            }
        }

        btnScore.setOnClickListener(){
            calScore(human,computer)
            resetGame(human,computer)
        }
    }

    private fun calScore(human: HumanDice, computer: ComputerDice){
        val humanTotalScoreTextView = findViewById<TextView>(R.id.tvTotalHumanScore)
        val computerTotalScoreTextView = findViewById<TextView>(R.id.tvTotalComputerScore)

        totalComputerScore += computer.totalScore()
        computerTotalScoreTextView.text = totalComputerScore.toString()

        totalHumanScore += human.totalScore()
        humanTotalScoreTextView.text = totalHumanScore.toString()
    }

    private fun resetGame(human:HumanDice, computer:ComputerDice){

        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)

        gameStart()

//            Disable dice lock after score is submitted
        human.getDieButtonArray().forEach {
            it.setOnClickListener { null }
        }

//            Resetting score after submitting the score
        tvHumanScore.text = "-"
        tvComputerScore.text = "-"

        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)
        btnScore.isEnabled = false
        btnThrow.isEnabled = true

        btnThrow.text = "Throw"

        // Disable all the dice buttons and set their images to the initial state
        human.resetDice()
        computer.resetDice()

        // Reset the number of rethrows
        rethrowCount = 0
    }

}