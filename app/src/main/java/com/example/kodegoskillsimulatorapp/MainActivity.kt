package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.kodegoskillsimulatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        gameListSetup()


        binding.tempbutton.setOnClickListener{
            val goToNextActivity = Intent(this, SelectClassActivity::class.java)
            startActivity(goToNextActivity)
            finish()
        }
    }

    private fun gameListSetup(){
        val gameList = arrayListOf("-- Select a game --", "Ragnarok Online (iRO)")

        val gameListAdapter = ArrayAdapter<String>(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gameList)
        gameListAdapter.setDropDownViewResource(
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        binding.spinnerSelectGame.adapter = gameListAdapter
    }
}