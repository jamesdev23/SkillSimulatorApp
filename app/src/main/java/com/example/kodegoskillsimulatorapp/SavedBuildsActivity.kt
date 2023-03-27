package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kodegoskillsimulatorapp.databinding.ActivitySavedBuildsBinding

class SavedBuildsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBuildsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBuildsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Saved Builds"

        binding.tempbutton.setOnClickListener{
            val goToNextActivity = Intent(this, MainActivity::class.java)
            startActivity(goToNextActivity)
            finish()
        }
    }
}