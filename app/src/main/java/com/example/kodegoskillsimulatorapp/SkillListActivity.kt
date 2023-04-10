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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.SkillAdapter
import com.example.kodegoskillsimulatorapp.dao.*
import com.example.kodegoskillsimulatorapp.databinding.ActivitySkillListBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddSkillBinding
import com.example.kodegoskillsimulatorapp.model.JobClass
import com.example.kodegoskillsimulatorapp.model.Build
import com.example.kodegoskillsimulatorapp.model.Skill
import com.example.kodegoskillsimulatorapp.observer.SkillBarObserver
import com.example.kodegoskillsimulatorapp.observer.SkillDataObserver
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SkillListActivity : AppCompatActivity(), SkillBarObserver, SkillDataObserver {
    private lateinit var binding: ActivitySkillListBinding
    private lateinit var skillAdapter: SkillAdapter
    private lateinit var dao: SkillDAO
    private lateinit var daoBuild: BuildDAO
    private var skills: ArrayList<Skill> = ArrayList()
    private var builds: ArrayList<Build> = ArrayList()

    private val maxSkillPoints = 49
    private var jobClassSelected: JobClass = JobClass()
    private var skillBuild: ArrayList<Skill> = ArrayList()
    private var skillBuildText:String = ""
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkillListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobClassSelected.gameName = intent.extras?.getString("data2").toString()
        jobClassSelected.name = intent.extras?.getString ("data3").toString()

        if(intent.extras?.containsKey("data4") == true){
            skillBuildText = intent.extras?.getString("data4").toString()
        }

        Log.i("skilllist game id", jobClassSelected.gameName)
        Log.i("skilllist class name", jobClassSelected.name)

        supportActionBar?.apply{
            title = jobClassSelected.name
            setDisplayHomeAsUpEnabled(true)
            displayOptions
        }

        if (skillBuildText.isEmpty()) {
            dao = SkillDAOSQLImpl(applicationContext)
            skills = dao.getSkillPerJob(jobClassSelected.gameName, jobClassSelected.name)
            skillAdapter = SkillAdapter(skills, this, this, this)
            binding.skillList.layoutManager = LinearLayoutManager(applicationContext)
            binding.skillList.adapter = skillAdapter
        }else {
            setSkillBuild(skillBuildText)
        }

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

//        binding.skillpointsTotal.text = " / ${maxSkillPoints.toString()}"


//        setSkillPointsLabelToDefault()

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
        menuInflater.inflate(R.menu.skilllist_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_skill -> {
                dialogAddSkill(this)
                return true
            }
            R.id.action_save -> {
                saveSkillData(skills)
                showSkillData(skills)
                Snackbar.make(binding.root, "Skill build saved.", Snackbar.LENGTH_SHORT).show()
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
    
    

    override fun getTotalProgress(list:MutableList<Int>) {
//        val totalSP = list.sum().toString()
//        val skillPointsTextView = findViewById<TextView>(R.id.skillpoints_current)
//        skillPointsTextView.text = totalSP
    }

    override fun checkSkillPoints(list: MutableList<Int>) {
//        var newProgressTotal = list.sum()
//        if(newProgressTotal <= maxSkillPoints) {
//            setSkillPointsLabelToDefault()
//        } else {
//            binding.skillpointsLabel.setTextColor(getColor(R.color.fire_engine_red))
//            binding.skillpointsCurrent.setTextColor(getColor(R.color.fire_engine_red))
//            binding.skillpointsTotal.setTextColor(getColor(R.color.fire_engine_red))
//        }
    }

//    private fun resetSkillPoints(jobclassData: Int) {
//        skillAdapter.updateSkill(skills)
//        binding.skillpointsCurrent.text = "0"
//        skillSimulatorSetup(dao, jobclassData)
//        Toast.makeText(
//            applicationContext,"Resetting skill points...",
//            Toast.LENGTH_SHORT
//        ).show()
//    }

//    private fun setSkillPointsLabelToDefault() {
//        // this code is optimized
//        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//        val isNightModeEnabled = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
//        val textColor = ContextCompat.getColor(this, if (isNightModeEnabled) R.color.white else R.color.black)
//
//        binding.skillpointsLabel.setTextColor(textColor)
//        binding.skillpointsCurrent.setTextColor(textColor)
//        binding.skillpointsTotal.setTextColor(textColor)
//    }

    override fun saveSkillData(skillData: ArrayList<Skill>) {
        if(skillData.isNotEmpty()) {
            var build = Build()
            val dao2: BuildDAO
            dao2 = BuildDAOSQLImpl(this)
            build.name = "Custom Build"
            build.jobClassName = jobClassSelected.name
            build.gameName = jobClassSelected.gameName
            build.description = "custom skill build"

            // using gson method
            val gson = Gson()
            build.dataText = gson.toJson(skillData)

            dao2.addBuild(build)
        }
    }

    override fun showSkillData(skillData: ArrayList<Skill>) {
        for(skill in skillData){
            Log.i("skill data", skill.name)
            Log.i("skill points", skill.currentLevel.toString())
        }
    }

    private fun isBuildNameExists(context: Context, buildName: String): Boolean {
        val daoBuild = BuildDAOSQLImpl(context)
        val skillBuild = daoBuild.getBuildByName(buildName)
        return skillBuild != null
    }

    private fun setSkillBuild(skillBuildText: String) {
        val gson = Gson()

        skillBuild = gson.fromJson(skillBuildText, object : TypeToken<ArrayList<Skill>>() {}.type)
        skillAdapter = SkillAdapter(skillBuild, this, this, this)
        binding.skillList.layoutManager = LinearLayoutManager(applicationContext)
        binding.skillList.adapter = skillAdapter
    }

    private fun dialogAddSkill(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAddSkillBinding: DialogAddSkillBinding =
                DialogAddSkillBinding.inflate(LayoutInflater.from(it))

            with(builder) {
                setPositiveButton("Add", DialogInterface.OnClickListener { _, _ ->
                    val dao: SkillDAO = SkillDAOSQLImpl(it)
                    val newSkill = Skill()

                    val addSkillName = dialogAddSkillBinding.editSkillName.text.toString()
                    val addSkillMaxLevel = dialogAddSkillBinding.editSkillMaxLevel.text.toString()
                    val addSkillMinLevel = dialogAddSkillBinding.editSkillMinLevel.text.toString()
                    val addSkillDescription = dialogAddSkillBinding.editSkillDescription.text.toString()

                    newSkill.name = addSkillName
                    newSkill.jobClassName = jobClassSelected.name
                    newSkill.gameName = jobClassSelected.gameName
                    newSkill.maxLevel = addSkillMaxLevel.toInt()
                    newSkill.minLevel = addSkillMinLevel.toInt()
                    newSkill.description = addSkillDescription

                    dao.addSkill(newSkill)
                    Log.i("new class", newSkill.name)
                    Log.i("class game id", jobClassSelected.gameName)
                    Log.i("class id", jobClassSelected.id.toString())
                    Log.i("new skill id", newSkill.id.toString())

                    var newSkills = dao.getSkillPerJob(jobClassSelected.gameName, jobClassSelected.name)
                    Log.i("skill list", newSkills.toString())
                    skillAdapter.updateSkill(newSkills)
                    skillAdapter.notifyDataSetChanged()
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    // Do something when user press the positive button
                })
                    .setView(dialogAddSkillBinding.root)
                    .create()
                    .show()
            }
        }
    }

    private fun addSkillToSkillList() {

    }
}