package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kodegoskillsimulatorapp.adapter.JobClassAdapter
import com.example.kodegoskillsimulatorapp.adapter.SkillAdapter
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.SavedBuildsDAO
import com.example.kodegoskillsimulatorapp.dao.SavedBuildsDAOSQLImpl
import com.example.kodegoskillsimulatorapp.dao.SkillDAO
import com.example.kodegoskillsimulatorapp.databinding.ActivitySavedBuildsBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.model.JobClass
import com.example.kodegoskillsimulatorapp.model.SavedBuild
import com.example.kodegoskillsimulatorapp.model.Skill
import kotlinx.coroutines.Job

class SavedBuildsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBuildsBinding

    private lateinit var skillAdapter: SkillAdapter
    private lateinit var skills: ArrayList<Skill>
    private lateinit var jobClasses: ArrayList<JobClass>

    private lateinit var daoSB: SavedBuildsDAO
    private lateinit var savedBuilds: ArrayList<SavedBuild>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBuildsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Saved Builds"

//        daoSB = SavedBuildsDAOSQLImpl(this)
//        savedBuilds = daoSB.getSavedBuilds()


    }

}