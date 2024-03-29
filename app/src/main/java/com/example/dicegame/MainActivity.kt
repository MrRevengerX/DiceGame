package com.example.dicegame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

//      Creating shared preferences to store the win and loss count
        val sharedPref = getSharedPreferences("gamePref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val winCount = sharedPref.getInt("wins", 0)
        val lossCount = sharedPref.getInt("losses", 0)
        editor.putInt("wins", winCount)
        editor.putInt("losses", lossCount)
        editor.apply()

        val winLossCountTextView = findViewById<TextView>(R.id.homeWinLossCount)
        winLossCountTextView.text = "H:$winCount/C:$lossCount"

        val inputTargetScore = findViewById<EditText>(R.id.inputTargetScore)

//      Adding the click listener to the info button
        val btnInfo = findViewById<Button>(R.id.btnInfo)
        btnInfo.setOnClickListener{
            showDevInfo()
        }

        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.setOnClickListener{
            val targetScore = inputTargetScore.text.toString()
//            If the target score is not entered, setting the value to 101 and send the value to the next activity
            if (targetScore.isBlank()) {
                val intent = Intent(this, GameScreen::class.java)
                intent.putExtra("targetScore", 101)
                startActivity(intent)
            } else {
                val maximumTargetScore = 5000
                try {
                    val targetScoreInt = targetScore.toInt()
                    if (targetScoreInt > maximumTargetScore) {
                        Toast.makeText(this, "Target score cannot be greater than $maximumTargetScore", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this, GameScreen::class.java)
                        intent.putExtra("targetScore", targetScoreInt)
                        startActivity(intent)
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Target score cannot be greater than $maximumTargetScore", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDevInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("Student ID: 20210226\n\n" +
                "Student Name: Ravindu Senarathna\n\n" +
                "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.\n")
        builder.setPositiveButton("OK") { _, _ ->

        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        val winLossCountTextView = findViewById<TextView>(R.id.homeWinLossCount)
        val winLossCount = getSharedPreferences("gamePref", Context.MODE_PRIVATE)
        val winCount = winLossCount.getInt("wins", 0)
        val lossCount = winLossCount.getInt("losses", 0)
        winLossCountTextView.text = "H:$winCount/C:$lossCount"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref = getSharedPreferences("gamePref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("wins", 0)
        editor.putInt("losses", 0)
        editor.apply()
    }
}