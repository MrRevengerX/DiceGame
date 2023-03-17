package com.example.dicegame


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class GameScreen : AppCompatActivity() {

//    Initializing variables
    private var totalHumanScore = 0
    private var totalComputerScore = 0
    private var targetScore = 0
    private var humanThrowCount: Int = 0
    private var computerThrowCount: Int = 0
    private var allowedHumanThrows: Int = 3
    private var allowedComputerThrows: Int = 3
    private lateinit var human: HumanDice
    private lateinit var computer: ComputerDice
    private var resultAnnounced = false
    private var tied = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        supportActionBar?.hide()

//        Retrieving the win and loss count from the shared preferences
        val sharedPref = getSharedPreferences("gamePref", Context.MODE_PRIVATE)
        val currentWins = sharedPref.getInt("wins", 0)
        val currentLosses = sharedPref.getInt("losses", 0)

        val winLossCountTextView = findViewById<TextView>(R.id.winLossCount)
        winLossCountTextView.text = "H:$currentWins/C:$currentLosses"


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
        human.resetDiceLock()
        computer.resetDiceLock()

//    Showing dice values on the screen
        drawDices(human)
        drawDices(computer)

//      Declaring buttons and textViews
        val btnThrow = findViewById<Button>(R.id.btnThrow)
        val btnScore = findViewById<Button>(R.id.btnScore)



//      Adding click event listener to throw button
        btnThrow.setOnClickListener{
            humanThrowCount++

//          Throw dice for human and computer
            human.throwDice()

//            Changing dice element according to the dice value
            drawDices(human)
//          Only roll the dice if it is the first throw
            if (computerThrowCount<1){
                computer.throwDice()
                drawDices(computer)
                computerThrowCount++
            }

//          Update total of the dice in the application
            calRollScore(human)
            calRollScore(computer)

//          Enable the score button after the first throw
            btnScore.isEnabled = true

//          Function to enable dice lock functionality
            lockDice()

//          Changing the text of the throw button after the first throw
            if (!tied && humanThrowCount>0){
                btnThrow.text = "Re-Throw"
            }

//          Reset game after all the given throws are used
            if (humanThrowCount >= allowedHumanThrows) {
                btnThrow.isEnabled = false
                btnScore.isEnabled = false

                if (!tied){
                    Toast.makeText(this, "Out of throws! Score will update soon", Toast.LENGTH_SHORT).show()
                }

//              Little delay show user what are the random dice values that he/she got in the last rethrow
                handler.postDelayed({
                    calScore()
                }, 1000)
            }
        }

//  Calculate score and resets the game if the score button clicked.
        btnScore.setOnClickListener{
            btnThrow.isEnabled = false
            btnScore.isEnabled = false
            calScore()
        }
    }

//  Update score for the roll
    private fun calRollScore(human:HumanDice){
        val tvHumanScore = findViewById<TextView>(R.id.tvHumanScore)
        tvHumanScore.text = human.totalScore().toString()
    }

    private fun calRollScore(computer: ComputerDice){
        val tvComputerScore = findViewById<TextView>(R.id.tvComputerScore)
        tvComputerScore.text = computer.totalScore().toString()
    }

//  Function to handle locking of dice
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
                if (human.getDice()[index].isDieEnabled() && human.getLockDiceCount() < 4) {
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


//  Function to show relevant dice images on the screen
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

//  Overloaded the function
    private fun drawDices(computer: ComputerDice){
        val btnComputerDie1 = findViewById<ImageButton>(R.id.ibtnComputer1)
        val btnComputerDie2 = findViewById<ImageButton>(R.id.ibtnComputer2)
        val btnComputerDie3 = findViewById<ImageButton>(R.id.ibtnComputer3)
        val btnComputerDie4 = findViewById<ImageButton>(R.id.ibtnComputer4)
        val btnComputerDie5 = findViewById<ImageButton>(R.id.ibtnComputer5)
        val computerDice = arrayOf(btnComputerDie1, btnComputerDie2, btnComputerDie3, btnComputerDie4, btnComputerDie5)

        computerDice.forEachIndexed { index, btn ->
            btn.setImageResource(computer.getDiceImageResource(computer.getDice()[index].getDieValue()))
        }

    }

//  Function to calculate the score and update the total score
    private fun calScore(){
        val winLossCountTextView = findViewById<TextView>(R.id.winLossCount)
        val humanTotalScoreTextView = findViewById<TextView>(R.id.tvTotalHumanScore)
        val computerTotalScoreTextView = findViewById<TextView>(R.id.tvTotalComputerScore)


//      Run the logic for computer to score
        computer = computerLogic(computer)
        calRollScore(computer)

        totalComputerScore += computer.totalScore()
        computerTotalScoreTextView.text = totalComputerScore.toString()

        totalHumanScore += human.totalScore()
        humanTotalScoreTextView.text = totalHumanScore.toString()

//    Initializing shared preferences
        val sharedPref = getSharedPreferences("gamePref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        var currentWins = sharedPref.getInt("wins", 0)
        var currentLosses = sharedPref.getInt("losses", 0)


//      Logic for decide win lose or draw
        if (totalHumanScore >= targetScore && totalHumanScore > totalComputerScore){
            currentWins++
            winLossCountTextView.text = "H:$currentWins/C:$currentLosses"
            resultAnnounced = true
            sharedPref.edit().putInt("win_count", currentLosses).apply()

// Reference : https://stackoverflow.com/questions/22655599/alertdialog-builder-with-custom-layout-and-edittext-cannot-access-view
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.winner_popup, null)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ -> }
            val dialog = builder.create()
            dialog.show()
        }

        else if (totalComputerScore >= targetScore && totalComputerScore > totalHumanScore){
            currentLosses++

            winLossCountTextView.text = "H:$currentWins/C:$currentLosses"
            resultAnnounced = true
            sharedPref.edit().putInt("win_count", currentLosses).apply()

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.loser_popup, null)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ -> }
            val dialog = builder.create()
            dialog.show()
        }
        else if (totalHumanScore >= targetScore && totalHumanScore == totalComputerScore){
            allowedHumanThrows = 1
            allowedComputerThrows = 1
            tied = true
            resetGame()
        }else{
            resetGame()
        }

        editor.putInt("wins", currentWins)
        editor.putInt("losses", currentLosses)
        editor.apply()

    }


/*
    Strategy for computer to score

   Here i haven't considered total score of computer and human as a variable to this logic
   because computer will win if it has the ability to use given throws to get maximum score
   from a given set of dices.

    1. If computer has a score more than 26, it will not roll the dice
    2. If computer has low score, lock dices larger than 4 and roll again
        - If computer score lower than before, it will throw again
    3. If computer has score below than 24 and does not have dice with value 5,6 this will throw all the dice
        - If computer score more than before it return the score
        - if computer got 6 or 5 after the throw, it will rethrow the dice
    4. If computer has dice with value 5,6 with score lower than 23, it will lock dices with value 5,6 and roll again
        - If computer score lower than before, it will throw again
    5. If computer has a score more than 23, it will lock dices with value 1,2,3 and roll again
        - If computer score lower than before, it will throw again
 */

private fun computerLogic(computer: ComputerDice): ComputerDice{
    computer.resetDiceLock()
    if (!tied && computerThrowCount in 1..2){
        val beforeTotalScore = computer.totalScore()
        computerThrowCount++
//            If computer has a score more than 26, it will not roll the dice
        if (computer.totalScore()>26){
            return computer
        }

//      If computer has low score, lock large dice and roll again
        if (computer.totalScore() in 5..10){

            computer.getDice().forEachIndexed{ index, die ->
                if (die.getDieValue() > 4 && computer.getLockDiceCount() < 4){
                    computer.getDice()[index].setDieEnabled(false)
                }
                else{
                    computer.getDice()[index].setDieEnabled(true)
                }
            }
            computer.throwDice()
            drawDices(computer)
            if (computer.totalScore() < beforeTotalScore+10){
                computerLogic(computer)
            }
            return computer
        }
//      If computer has score lower than 23 and it doesn't have 6 or 5, it will roll all the dice
        if ((6 !in computer.getDieValueArray() || 5 !in computer.getDieValueArray()) && computer.totalScore()<=23){

            computer.throwDice()
            drawDices(computer)

            if (computer.totalScore() > beforeTotalScore+10){
                return computer
            }
            if (6 !in computer.getDieValueArray() || 5 !in computer.getDieValueArray() || beforeTotalScore+5 > computer.totalScore()){
                computerLogic(computer)
            }
            return computer
        }
//      if computer has a score less than 23, and it has a 6 or 5, it will lock the dice and roll again
        if ((6 in computer.getDieValueArray() || 5 in computer.getDieValueArray()) && computer.totalScore()<=23){
            computer.getDice().forEachIndexed{ index, die ->
                if (die.getDieValue() == 6 && computer.getLockDiceCount() < 4){
                    computer.getDice()[index].setDieEnabled(false)
                }else if(die.getDieValue() == 5 && computer.getLockDiceCount() < 4){
                    computer.getDice()[index].setDieEnabled(false)
                }
                else{
                    computer.getDice()[index].setDieEnabled(true)
                }
            }
            computer.throwDice()
            drawDices(computer)
            if (beforeTotalScore+5 > computer.totalScore()){
                computerLogic(computer)
            }
            return computer
        }
//      if computer has a score more than 23, it will dice with value 1,2,3 and roll again
        if (computer.totalScore()>=23){
            computer.getDice().forEachIndexed{ index, die ->
                if (die.getDieValue() <= 3){
                    computer.getDice()[index].setDieEnabled(true)
                }else{
                    computer.getDice()[index].setDieEnabled(false)
                }
            }
            computer.throwDice()
            drawDices(computer)
            if (beforeTotalScore > computer.totalScore()){
                computerLogic(computer)
            }
            return computer
        }
        return computer
    }else{
        return computer
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

//  Disable dice lock after score is submitted
    humanDice.forEach {
        it.setOnClickListener { null }
    }

//  Resetting score after submitting the score
    tvHumanScore.text = "-"
    tvComputerScore.text = "-"

    val btnThrow = findViewById<Button>(R.id.btnThrow)
    val btnScore = findViewById<Button>(R.id.btnScore)
    btnScore.isEnabled = false
    btnThrow.isEnabled = true

    btnThrow.text = "Throw"


//  Disable all the dice buttons and set their images to the initial state
    human = HumanDice(arrayOf(1,2,3,4,5))
    computer = ComputerDice(arrayOf(1,2,3,4,5))

    drawDices(human)
    drawDices(computer)

    // Reset the number of rethrows
    humanThrowCount = 0
    computerThrowCount = 0
}

//    Function to save data on device rotation
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("totalHumanScore", totalHumanScore)
    outState.putInt("totalComputerScore", totalComputerScore)
    outState.putInt("humanThrowCount", humanThrowCount)
    outState.putInt("computerThrowCount", computerThrowCount)
    outState.putInt("allowedHumanThrows", allowedHumanThrows)
    outState.putInt("allowedComputerThrows", allowedComputerThrows)
    outState.putBoolean("resultAnnounced", resultAnnounced)
    outState.putBoolean("tied", tied)
    outState.putSerializable("computerDice", computer)
    outState.putSerializable("humanDice", human)
}

//  Function to restore data on device rotation
override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    totalHumanScore = savedInstanceState.getInt("totalHumanScore")
    totalComputerScore = savedInstanceState.getInt("totalComputerScore")
    humanThrowCount = savedInstanceState.getInt("humanThrowCount")
    computerThrowCount = savedInstanceState.getInt("computerThrowCount")
    allowedHumanThrows = savedInstanceState.getInt("allowedHumanThrows")
    allowedComputerThrows = savedInstanceState.getInt("allowedComputerThrows")
    resultAnnounced = savedInstanceState.getBoolean("resultAnnounced")
    tied = savedInstanceState.getBoolean("tied")

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
    if (tied){
        tvHumanDice.text = human.totalScore().toString()
        tvComputerDice.text = computer.totalScore().toString()
        btnThrow.text = "Throw"
        btnScore.isEnabled = true
    }
    else if (humanThrowCount>0) {
        tvHumanDice.text = human.totalScore().toString()
        tvComputerDice.text = computer.totalScore().toString()
        btnThrow.text = "Re-throw"
        btnScore.isEnabled = true
    }

    if (resultAnnounced){
        btnThrow.isEnabled = false
        btnScore.isEnabled = false
    }

    drawDices(human)
    drawDices(computer)
    if (humanThrowCount>0){
        lockDice()
    }
}
}