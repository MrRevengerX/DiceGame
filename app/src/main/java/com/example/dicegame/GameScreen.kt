package com.example.dicegame


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class GameScreen : AppCompatActivity() {

//    Initializing variables
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

//        Retrieving the target score from the previous activity
        targetScore = intent.getIntExtra("targetScore", 0)

//        Set the target score according to the value retrieved target value
        val tvTargetScore = findViewById<TextView>(R.id.tvTargetScore)
        tvTargetScore.text = targetScore.toString()

        gameStart()
    }

//    Function to start the game
    private fun gameStart(){
        val handler = Handler()

//    Initializing the dice objects with mock values
        human = HumanDice(arrayOf(1,2,3,4,5))
        computer = ComputerDice(arrayOf(1,2,3,4,5))

//    Showing dice values on the screen
        drawDices(human)
        drawDices(computer)

//        Declaring buttons and textViews
        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)
        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)


//        Adding click event listener to throw button
        btnThrow.setOnClickListener{
            rethrowCount++

//            Throw dice for human and computer
            human.throwDice()

//            Changing dice element according to the dice value
            drawDices(human)
//            Only roll the dice if it is the first throw
            if (rethrowCount<=1){
                computer.throwDice()
                drawDices(computer)
            }

//            Updating the addition of the 5 dice values for human and computer
            tvHumanScore.text = human.totalScore().toString()
            tvComputerScore.text = computer.totalScore().toString()

//            Enable the score button after the first throw
            btnScore.isEnabled = true

//          Function to enable dice lock functionality
            lockDice()

//          Changing the text of the throw button after the first throw
            if (rethrowCount>0){
                btnThrow.text = "Re-Throw"
            }

//                Reset game after all the given throws are used
            if (rethrowCount >= allowedThrows) {
                btnThrow.isEnabled = false
                btnScore.isEnabled = false

                Toast.makeText(this, "Out of throws! Score will update soon", Toast.LENGTH_SHORT).show()
//                Little delay before reset the game, to show user what are the random dice values that he/she got in the last rethrow
                handler.postDelayed({
                    calScore()
//                    resetGame()
                }, 1000)
            }
        }

//    Calculate score and resets the game if the score button clicked.
        btnScore.setOnClickListener{
            btnThrow.isEnabled = false
            btnScore.isEnabled = false
            calScore()
//            resetGame()
        }
    }

//    Function to handle locking of dice
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

//                Lock only maximum of 4 dice
                if (human.getDice()[index].isDieEnabled() && human.lockDiceCount() < 4) {
                    btn.setImageResource(human.dieEnableToggle(index, valueOfTheDice))
                    human.getDice()[index].setDieEnabled(false)
                }
//                Unlock dice if it is already locked
                else if (!human.getDice()[index].isDieEnabled()) {
                    btn.setImageResource(human.dieEnableToggle(index, valueOfTheDice))
                    human.getDice()[index].setDieEnabled(true)
                }
//                Show error message to the user if he/she tries to lock more than 4 dice
                else {
                    Toast.makeText(this, "You can only lock 4 dice", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


//    Function to show relevant dice images on the screen
    private fun drawDices(human: HumanDice){
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

    }

//    Overloaded the function
    private fun drawDices(computer: ComputerDice){
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

//    Function to calculate the score and update the total score
    private fun calScore(){
        val humanTotalScoreTextView = findViewById<TextView>(R.id.tvTotalHumanScore)
        val computerTotalScoreTextView = findViewById<TextView>(R.id.tvTotalComputerScore)

        totalComputerScore += computer.totalScore()
        computerTotalScoreTextView.text = totalComputerScore.toString()

        totalHumanScore += human.totalScore()
        humanTotalScoreTextView.text = totalHumanScore.toString()

//    Logic for decide win lose or draw
        if (totalHumanScore >= targetScore && totalHumanScore > totalComputerScore){
            Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show()
        }

        else if (totalComputerScore >= targetScore && totalComputerScore > totalHumanScore){
            Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Score updated!", Toast.LENGTH_SHORT).show()
            resetGame()
        }

    }

//    Function to reset all the functionality and visual elements of the game
    private fun resetGame(){
        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)


        val btnHumanDie1 = findViewById<ImageButton>(R.id.ibtnHuman1)
        val btnHumanDie2 = findViewById<ImageButton>(R.id.ibtnHuman2)
        val btnHumanDie3 = findViewById<ImageButton>(R.id.ibtnHuman3)
        val btnHumanDie4 = findViewById<ImageButton>(R.id.ibtnHuman4)
        val btnHumanDie5 = findViewById<ImageButton>(R.id.ibtnHuman5)
        val humanDice = arrayOf(btnHumanDie1, btnHumanDie2, btnHumanDie3, btnHumanDie4, btnHumanDie5)

//            Disable dice lock after score is submitted
        humanDice.forEach {
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

        Toast.makeText(this, "Resetting game", Toast.LENGTH_SHORT).show()
//        // Disable all the dice buttons and set their images to the initial state
        human = HumanDice(arrayOf(1,2,3,4,5))
        computer = ComputerDice(arrayOf(1,2,3,4,5))


        drawDices(human)
        drawDices(computer)

        // Reset the number of rethrows
        rethrowCount = 0

//        gameStart()
    }

//    Function to save data on device rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("totalHumanScore", totalHumanScore)
        outState.putInt("totalComputerScore", totalComputerScore)
        outState.putInt("rethrowCount", rethrowCount)
        outState.putSerializable("computerDice", computer)
        outState.putSerializable("humanDice", human)
    }

//  Function to restore data on device rotation
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

        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)

//    Recreating visual elements as it should be after screen rotation
        if (rethrowCount>0) {
            tvHumanDice.text = human.totalScore().toString()
            tvComputerDice.text = computer.totalScore().toString()
            btnThrow.text = "Re-throw"
            btnScore.isEnabled = true
        }
//        Toast.makeText(this, "Restoring data", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "${human.getDice()[0].getDieValue()},${human.getDice()[1].getDieValue()},${human.getDice()[2].getDieValue()},${human.getDice()[3].getDieValue()},${human.getDice()[4].getDieValue()}", Toast.LENGTH_SHORT).show()

        drawDices(human)
        drawDices(computer)
        if (rethrowCount>0){
            lockDice()
        }

    }
//
//    private fun reDrawDices(resHuman: HumanDice, resComputer: ComputerDice) {
////        Get value of each dice and show them in a toast
//        Toast.makeText(this, "${resHuman.getDice()[0].getDieValue()},${resHuman.getDice()[1].getDieValue()},${resHuman.getDice()[2].getDieValue()},${resHuman.getDice()[3].getDieValue()},${resHuman.getDice()[4].getDieValue()}", Toast.LENGTH_SHORT).show()
//        drawDices(resHuman, resComputer)
//
//        if (rethrowCount>0){
//            lockDice()
//        }
//
//    }
}