package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kodegoskillsimulatorapp.databinding.ActivitySkillListBinding

class SkillListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySkillListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkillListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Class Name"

        binding.tempbutton.setOnClickListener{
            val goToNextActivity = Intent(this, SavedBuildsActivity::class.java)
            startActivity(goToNextActivity)
        }
    }
}