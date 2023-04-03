package com.example.kodegoskillsimulatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kodegoskillsimulatorapp.dao.BuildDAO
import com.example.kodegoskillsimulatorapp.databinding.ActivitySavedBuildsBinding
import com.example.kodegoskillsimulatorapp.model.Build
import com.example.kodegoskillsimulatorapp.model.Skill

class SavedBuildsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBuildsBinding

    private lateinit var dao: BuildDAO
    private lateinit var builds: ArrayList<Build>
    private lateinit var skills: ArrayList<Skill>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBuildsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Saved Builds"

//        daoSB = SavedBuildsDAOSQLImpl(this)
//        savedBuilds = daoSB.getSavedBuilds()


    }

}