package com.example.dicegame


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class GameScreen : AppCompatActivity() {

    private var totalHumanScore = 0
    private var totalComputerScore = 0
    private var targetScore = 0
    private var rethrowCount: Int = 0
    private var allowedThrows: Int = 3
    private lateinit var human: HumanDice
    private lateinit var computer: ComputerDice


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        supportActionBar?.hide()

        targetScore = intent.getIntExtra("targetScore", 0)

//        Set the target score to the user entered value
        val tvTargetScore = findViewById<TextView>(R.id.tvTargetScore)
        tvTargetScore.text = targetScore.toString()

        gameStart()
    }
//
    private fun gameStart(){
        val handler = Handler()

        human = HumanDice(arrayOf(1,2,3,4,5))
        computer = ComputerDice(arrayOf(1,2,3,4,5))

        drawDices(human,computer)

//        Declaring buttons and textViews
        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)
        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)




//        Adding click event listener to throw button
        btnThrow.setOnClickListener{
            rethrowCount++
            human.throwDice()
            computer.throwDice()
            drawDices(human,computer)

//            Enable the score button after the first throw
            btnScore.isEnabled = true

//            human.resetDiceEnabled()
            lockDice()



//            Enable locking dice after the first throw
//            humanDice.forEachIndexed() { index, btn ->
//                val valueOfTheDice = human.getDice()[index].getDieValue()
//                btn.setImageResource(human.dieEnableToggle(valueOfTheDice))
//            }

//            Throw dice for human and computer
            tvHumanScore.text = human.totalScore().toString()
            tvComputerScore.text = computer.totalScore().toString()

//            If rethrow count is greater than 2, disable the throw button and add score to the total score
            if (rethrowCount > 2) {
                btnThrow.isEnabled = false

                Toast.makeText(this, "Out of throws! Score will update soon", Toast.LENGTH_SHORT).show()
//                Reset game after using all throws
                handler.postDelayed({
                    calScore()
                    resetGame()
                }, 1000)
            }else{
//                Toast.makeText(this, "${allowedThrows-rethrowCount} throws left.", Toast.LENGTH_SHORT).show()
            }
        }

        btnScore.setOnClickListener{
            calScore()
            resetGame()
        }
    }

    private fun lockDice(){

        val btnHumanDie1 = findViewById<ImageButton>(R.id.ibtnHuman1)
        val btnHumanDie2 = findViewById<ImageButton>(R.id.ibtnHuman2)
        val btnHumanDie3 = findViewById<ImageButton>(R.id.ibtnHuman3)
        val btnHumanDie4 = findViewById<ImageButton>(R.id.ibtnHuman4)
        val btnHumanDie5 = findViewById<ImageButton>(R.id.ibtnHuman5)
        val humanDice = arrayOf(btnHumanDie1, btnHumanDie2, btnHumanDie3, btnHumanDie4, btnHumanDie5)

        humanDice.forEachIndexed{ index, btn ->
            btn.setOnClickListener{
                val valueOfTheDice = human.getDice()[index].getDieValue()
                if (human.getDice()[index].isDieEnabled() && human.lockDiceCount() < 4) {
                    btn.setImageResource(human.dieEnableToggle(index, valueOfTheDice))
                    human.getDice()[index].setDieEnabled(false)
                } else if (!human.getDice()[index].isDieEnabled()) {
                    btn.setImageResource(human.dieEnableToggle(index, valueOfTheDice))
                    human.getDice()[index].setDieEnabled(true)
                } else {
                    Toast.makeText(this, "You can only lock 4 dice", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun drawDices(human: HumanDice, computer: ComputerDice){
        val btnHumanDie1 = findViewById<ImageButton>(R.id.ibtnHuman1)
        val btnHumanDie2 = findViewById<ImageButton>(R.id.ibtnHuman2)
        val btnHumanDie3 = findViewById<ImageButton>(R.id.ibtnHuman3)
        val btnHumanDie4 = findViewById<ImageButton>(R.id.ibtnHuman4)
        val btnHumanDie5 = findViewById<ImageButton>(R.id.ibtnHuman5)
        val humanDice = arrayOf(btnHumanDie1, btnHumanDie2, btnHumanDie3, btnHumanDie4, btnHumanDie5)

        humanDice.forEachIndexed{ index, btn ->
            if (human.getDice()[index].isDieEnabled())
                btn.setImageResource(human.getDiceImageResource(human.getDice()[index].getDieValue()))
            else
                btn.setImageResource(human.getLockedDiceImageResource(human.getDice()[index].getDieValue()))
        }

        val btnComputerDie1 = findViewById<ImageButton>(R.id.ibtnComputer1)
        val btnComputerDie2 = findViewById<ImageButton>(R.id.ibtnComputer2)
        val btnComputerDie3 = findViewById<ImageButton>(R.id.ibtnComputer3)
        val btnComputerDie4 = findViewById<ImageButton>(R.id.ibtnComputer4)
        val btnComputerDie5 = findViewById<ImageButton>(R.id.ibtnComputer5)
        val computerDice = arrayOf(btnComputerDie1, btnComputerDie2, btnComputerDie3, btnComputerDie4, btnComputerDie5)

        computerDice.forEachIndexed{ index, btn ->
            btn.setImageResource(computer.getDiceImageResource(computer.getDice()[index].getDieValue()))
        }
    }

    private fun calScore(){
        val humanTotalScoreTextView = findViewById<TextView>(R.id.tvTotalHumanScore)
        val computerTotalScoreTextView = findViewById<TextView>(R.id.tvTotalComputerScore)

        totalComputerScore += computer.totalScore()
        computerTotalScoreTextView.text = totalComputerScore.toString()

        totalHumanScore += human.totalScore()
        humanTotalScoreTextView.text = totalHumanScore.toString()
    }

    private fun resetGame(){


        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)

//            Disable dice lock after score is submitted
//        human.getDieButtonArray().forEach {
//            it.setOnClickListener { null }
//        }

//            Resetting score after submitting the score
        tvHumanScore.text = "-"
        tvComputerScore.text = "-"

        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)
        btnScore.isEnabled = false
        btnThrow.isEnabled = true

        btnThrow.text = "Throw"

        Toast.makeText(this, "Resetting game", Toast.LENGTH_SHORT).show()
//        // Disable all the dice buttons and set their images to the initial state
        human = HumanDice(arrayOf(1,2,3,4,5))
        computer = ComputerDice(arrayOf(1,2,3,4,5))


        drawDices(human,computer)

        // Reset the number of rethrows
        rethrowCount = 0

//        gameStart()
    }

//    save data when rotating device
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("totalHumanScore", totalHumanScore)
        outState.putInt("totalComputerScore", totalComputerScore)
        outState.putInt("rethrowCount", rethrowCount)
        outState.putSerializable("computerDice", computer)
        outState.putSerializable("humanDice", human)
    }
//
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        totalHumanScore = savedInstanceState.getInt("totalHumanScore")
        totalComputerScore = savedInstanceState.getInt("totalComputerScore")
        rethrowCount = savedInstanceState.getInt("rethrowCount")

        human = savedInstanceState.getSerializable("humanDice") as HumanDice
        computer = savedInstanceState.getSerializable("computerDice") as ComputerDice

        val tvTotalHumanScore = findViewById<TextView>(R.id.tvTotalHumanScore)
        val tvTotalComputerScore = findViewById<TextView>(R.id.tvTotalComputerScore)


        tvTotalHumanScore.text = totalHumanScore.toString()
        tvTotalComputerScore.text = totalComputerScore.toString()


        val tvHumanDice = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerDice = findViewById<TextView>(R.id.tvComputerScore)

        if (rethrowCount>0) {
            tvHumanDice.text = human.totalScore().toString()
            tvComputerDice.text = computer.totalScore().toString()
        }
//        Toast.makeText(this, "Restoring data", Toast.LENGTH_SHORT).show()

        reDrawDices(human, computer)

    }
//
    private fun reDrawDices(resHuman: HumanDice, resComputer: ComputerDice) {
//        Get value of each dice and show them in a toast
        Toast.makeText(this, "${resHuman.getDice()[0].getDieValue()},${resHuman.getDice()[1].getDieValue()},${resHuman.getDice()[2].getDieValue()},${resHuman.getDice()[3].getDieValue()},${resHuman.getDice()[4].getDieValue()}", Toast.LENGTH_SHORT).show()
        drawDices(resHuman, resComputer)

    if (rethrowCount>0){
        lockDice()
    }

    }
}