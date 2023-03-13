package com.example.dicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        #get info btn
        val btnInfo = findViewById<Button>(R.id.btnInfo)
        btnInfo.setOnClickListener(){
            showDevInfo()
        }
    }

    fun showDevInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("Student ID: 20210226\n\n" +
                "Student Name: Ravindu Senarathna\n\n" +
                "I confirm that I understand what plagiarism is and have read and\n" +
                "understood the section on Assessment Offences in the Essential\n" +
                "Information for Students. The work that I have submitted is\n" +
                "entirely my own. Any work from other authors is duly referenced\n" +
                "and acknowledged.\n")
        builder.setPositiveButton("OK") { dialog, which ->
            // do something when OK button is clicked
        }
        val dialog = builder.create()
        dialog.show()
    }
}