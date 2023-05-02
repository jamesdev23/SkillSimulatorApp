package com.example.kodegoskillsimulatorapp

import android.app.UiModeManager
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
import androidx.core.content.ContextCompat
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
    private var skills: ArrayList<Skill> = ArrayList()
    private var jobClassSelected: JobClass = JobClass()
    private var skillBuild: ArrayList<Skill> = ArrayList()
    private var skillBuildText:String = ""
    private val maxSkillPoints = 49
    private var totalSP = 0
    private var remainingSP = 0



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

        supportActionBar?.apply {
            title = jobClassSelected.name
            setDisplayHomeAsUpEnabled(true)
            displayOptions
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

        if (skillBuildText.isEmpty()) {
            setSkillListDefault(jobClassSelected)
        } else {
            setSkillBuild(skillBuildText)
        }

        setSkillPointsLabelToDefault()

    }
    private fun setSkillListDefault(jobClass: JobClass){
        dao = SkillDAOSQLImpl(applicationContext)
        skills = dao.getSkillPerJob(jobClass.gameName, jobClass.name)
        skillAdapter = SkillAdapter(skills, this, this)
        binding.skillList.layoutManager = LinearLayoutManager(applicationContext)
        binding.skillList.adapter = skillAdapter
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
            R.id.action_reset_all_skills -> {
                resetSkillPoints(jobClassSelected)
                return true
            }
            R.id.action_save -> {
                saveSkillData(skills)
                showSkillData(skills)
                toast("Skill Build Saved.")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun getTotalProgress(list:MutableList<Int>) {
        totalSP = list.sum()
        val setTotalSP = "Skill Points: $totalSP/$maxSkillPoints"
        binding.textSkillPoints.text = setTotalSP
    }

    override fun checkSkillPoints(list: MutableList<Int>) {
        val newProgressTotal = list.sum()
        if(newProgressTotal <= maxSkillPoints) {
            setSkillPointsLabelToDefault()
        } else {
            binding.textSkillPoints.setTextColor(getColor(R.color.fire_engine_red))
        }
    }

    private fun resetSkillPoints(jobClass: JobClass) {
        skillAdapter.updateSkill(skills)
        val defaultText = "Skill Points: 0/$maxSkillPoints"
        binding.textSkillPoints.text = defaultText
        setSkillListDefault(jobClass)
        setSkillPointsLabelToDefault()
        toast("Skill List Reset.")
    }

    private fun setSkillPointsLabelToDefault() {
        // this code is optimized
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isNightModeEnabled = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        val textColor = ContextCompat.getColor(this, if (isNightModeEnabled) R.color.white else R.color.black)

        binding.textSkillPoints.setTextColor(textColor)
    }

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

    private fun setSkillBuild(skillBuildText: String) {
        Log.d("SKILL BUILD TEXT FROM SKILLLIST", skillBuildText)
        val gson = Gson()

        skillBuild = gson.fromJson(skillBuildText, object : TypeToken<ArrayList<Skill>>() {}.type)
        skillAdapter = SkillAdapter(skillBuild, this, this)
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
                    skillAdapter.notifyItemInserted(newSkill.id)
                    toast("Added ${newSkill.name} skill.")
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

    private fun toast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}