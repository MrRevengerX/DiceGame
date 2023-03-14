package com.example.dicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat

class GameScreen : AppCompatActivity() {
    private var rethrowCount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

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

        val human = HumanDice(humanDice)
        val computer = ComputerDice(computerDice)

        val btnThrow = findViewById<Button>(R.id.btnThrow)

        btnThrow.setOnClickListener(){
            rethrowCount++
            if (rethrowCount > 0)
                btnThrow.text = "Re-Throw"


            if (rethrowCount > 2) {
                btnThrow.isEnabled = false
                btnThrow.setBackgroundColor(ContextCompat.getColor(this, R.color.Gray))
            }
            human.throwDice()
            computer.throwDice()
        }

    }
}