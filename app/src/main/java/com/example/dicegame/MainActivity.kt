package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val inputTargetScore = findViewById<EditText>(R.id.inputTargetScore)

//        #get info btn
        val btnInfo = findViewById<Button>(R.id.btnInfo)
        btnInfo.setOnClickListener(){
            showDevInfo()
        }

        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.setOnClickListener(){
            val targetScore = inputTargetScore.text.toString()
//            If the target score is not entered, setting the value to 101 and send the value to the next activity
            if (targetScore == "") {
                val intent = Intent(this, GameScreen::class.java)
                intent.putExtra("targetScore", 101)
                startActivity(intent)
            } else {
                val intent = Intent(this, GameScreen::class.java)
                intent.putExtra("targetScore", targetScore.toInt())
                startActivity(intent)
            }
        }
    }

    fun showDevInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("Student ID: 20210226\n\n" +
                "Student Name: Ravindu Senarathna\n\n" +
                "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.\n")
        builder.setPositiveButton("OK") { dialog, which ->
            // do something when OK button is clicked
        }
        val dialog = builder.create()
        dialog.show()
    }
}