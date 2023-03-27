package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kodegoskillsimulatorapp.databinding.ActivitySelectClassBinding

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Select Class"

        binding.tempbutton.setOnClickListener{
            val goToNextActivity = Intent(this, SkillListActivity::class.java)
            startActivity(goToNextActivity)
        }
    }
}