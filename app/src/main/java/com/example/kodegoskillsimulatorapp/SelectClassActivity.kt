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
import com.example.kodegoskillsimulatorapp.adapter.JobClassAdapter
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.JobClassDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySelectClassBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddClassBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.model.JobClass
import com.google.android.material.snackbar.Snackbar

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding

    private lateinit var jobClassAdapter: JobClassAdapter
    private lateinit var dao: JobClassDAO
    private var jobClasses: ArrayList<JobClass> = ArrayList()
    private var selectedGame: Game = Game()
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieving game name
        val bundle = intent.extras
        selectedGame.name = bundle?.getString("DATA_GAME_NAME").toString()
        Log.i("DATA GAME NAME", selectedGame.name)

        // setting up action bar
        supportActionBar?.apply{
            title = "Select Class"
            setDisplayHomeAsUpEnabled(true)
            displayOptions
        }

        // setting up class list

        dao = JobClassDAOSQLImpl(applicationContext)
        jobClasses = dao.getJobclassPerGame(selectedGame.name)
        jobClassAdapter = JobClassAdapter(jobClasses, this)
//        binding.classList.layoutManager = LinearLayoutManager(applicationContext)
        binding.classList.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.classList.adapter = jobClassAdapter

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.skill_simulator_tab -> {
                    val main = Intent(this, MainActivity::class.java)
                    startActivity(main)
                    true
                }
                R.id.saved_builds_tab -> {
                    val savedBuilds = Intent(this, SavedBuildsActivity::class.java)
                    startActivity(savedBuilds)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
                finish()
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

                    jobClass.gameName = selectedGame.name
                    jobClass.name = addJobClassName
                    Log.i("add class name", jobClass.name)
                    Log.i("add class game id", jobClass.gameName)

                    dao.addJobclass(jobClass)

                    var newJobClass = dao.getJobclassPerGame(selectedGame.name)
                    Log.i("class list", newJobClass.toString())
                    jobClassAdapter.updateJobClass(newJobClass)
                    jobClassAdapter.notifyDataSetChanged()

                    Snackbar.make(binding.root, "Added new class.", Snackbar.LENGTH_SHORT).show()
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