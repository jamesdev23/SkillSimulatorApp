package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.JobClassAdapter
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.JobClassDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySelectClassBinding
import com.example.kodegoskillsimulatorapp.model.JobClass

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding
    private lateinit var jobClassAdapter: JobClassAdapter
    private var jobClasses: ArrayList<JobClass> = ArrayList()
    private lateinit var dao: JobClassDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Select Class"

        dao = JobClassDAOSQLImpl(applicationContext)
        jobClasses = dao.getJobclass()

        jobClassAdapter = JobClassAdapter(jobClasses, this)
        binding.classList.layoutManager = LinearLayoutManager(applicationContext)
        binding.classList.adapter = jobClassAdapter

        binding.tempbutton.setOnClickListener{
            val goToNextActivity = Intent(this, SkillListActivity::class.java)
            startActivity(goToNextActivity)
            finish()
        }
    }
}