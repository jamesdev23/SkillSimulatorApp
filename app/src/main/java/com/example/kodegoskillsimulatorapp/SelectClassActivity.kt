package com.example.kodegoskillsimulatorapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.JobClassAdapter
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.JobClassDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySelectClassBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddClassBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.model.JobClass

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding

    private lateinit var jobClassAdapter: JobClassAdapter
    private lateinit var dao: JobClassDAO
    private lateinit var jobClasses: ArrayList<JobClass>
    private var gameSelected: Game = Game()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameSelected.name = intent.extras?.getString("data").toString()

        Log.i("selectclass game id", gameSelected.id.toString())

        supportActionBar?.title = "Select Class"

        dao = JobClassDAOSQLImpl(applicationContext)
        jobClasses = dao.getJobclassPerGame(gameSelected.name)
        jobClassAdapter = JobClassAdapter(jobClasses, this)
//        binding.classList.layoutManager = LinearLayoutManager(applicationContext)
        binding.classList.layoutManager = GridLayoutManager(applicationContext, 3)
        binding.classList.adapter = jobClassAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.jobclass_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_class -> {
                dialogAddClass(this)
                return true
            }
            R.id.action_saved_builds -> {
                val goToSavedBuilds = Intent(this, SavedBuildsActivity::class.java)
                startActivity(goToSavedBuilds)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun dialogAddClass(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAddClassBinding: DialogAddClassBinding =
                DialogAddClassBinding.inflate(LayoutInflater.from(it))

            with(builder) {
                setPositiveButton("Add", DialogInterface.OnClickListener { _, _ ->
                    val dao: JobClassDAO = JobClassDAOSQLImpl(it)
                    val jobClass = JobClass()

                    val addJobClassName = dialogAddClassBinding.editClassName.text.toString()

                    jobClass.gameName = gameSelected.name
                    jobClass.name = addJobClassName
                    Log.i("add class name", jobClass.name)
                    Log.i("add class game id", jobClass.gameName)

                    dao.addJobclass(jobClass)

                    var newJobClass = dao.getJobclassPerGame(gameSelected.name)
                    Log.i("class list", newJobClass.toString())
                    jobClassAdapter.updateJobClass(newJobClass)
                    jobClassAdapter.notifyDataSetChanged()
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    // Do something when user press the positive button
                })
                    .setView(dialogAddClassBinding.root)
                    .create()
                    .show()
            }
        }
    }
}