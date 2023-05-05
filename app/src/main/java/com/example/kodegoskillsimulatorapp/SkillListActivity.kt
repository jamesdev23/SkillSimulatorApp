package com.example.kodegoskillsimulatorapp

import android.app.UiModeManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
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
import com.google.gson.Gson

class SkillListActivity : AppCompatActivity(), SkillBarObserver, SkillDataObserver {
    private lateinit var binding: ActivitySkillListBinding
    private lateinit var skillAdapter: SkillAdapter
    private lateinit var dao: SkillDAO
    private var selectedJobClass: JobClass = JobClass()
    private var skills: ArrayList<Skill> = ArrayList()
    private var skillBuild: ArrayList<Skill> = ArrayList()
    private var skillBuildText: String = ""
    private var totalSP = 0
    private var maxSkillPoints = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkillListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        selectedJobClass.gameName = bundle?.getString("DATA_GAME_NAME").toString()
        selectedJobClass.name = bundle?.getString ("DATA_JOB_CLASS_NAME").toString()

        if(bundle?.containsKey("DATA_SKILL_BUILD") == true){
            skillBuild = bundle.getParcelableArrayList<Skill>("DATA_SKILL_BUILD") as ArrayList<Skill>
        }

        maxSkillPoints = selectedJobClass.maxSkillPoints

        Log.d("DATA GAME NAME", selectedJobClass.gameName)
        Log.d("DATA JOB CLASS NAME", selectedJobClass.name)
        Log.d("DATA SKILL BUILD", skillBuild.toString())
        Log.d("DATA JOB MAX SP", selectedJobClass.maxSkillPoints.toString())

        supportActionBar?.apply {
            title = selectedJobClass.name
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

        when {
            skillBuild.isEmpty() ->
                setSkillListDefault(selectedJobClass)
            else ->
                setSkillBuild(skillBuild)
        }

        binding.skillList.layoutManager = LinearLayoutManager(applicationContext)
        binding.skillList.adapter = skillAdapter


        setSkillPointsLabelToDefault()

    }
    private fun setSkillListDefault(jobClass: JobClass){
        dao = SkillDAOSQLImpl(applicationContext)
        skills = dao.getSkillPerJob(jobClass.gameName, jobClass.name)
        skillAdapter = SkillAdapter(skills, this, this)
    }

    private fun setSkillBuild(skillBuild: ArrayList<Skill>) {
        skillAdapter = SkillAdapter(skillBuild, this, this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        saveSkillData(skillBuild)
        super.onBackPressed()
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
                resetSkillPoints(selectedJobClass)
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
        toast("Skill List reset.")
    }

    private fun setSkillPointsLabelToDefault() {
        // this code is optimized
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isNightModeEnabled = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        val textColor = ContextCompat.getColor(this, if (isNightModeEnabled) R.color.white else R.color.black)

        binding.textSkillPoints.setTextColor(textColor)
    }

    override fun saveSkillData(skillData: ArrayList<Skill>) {
        val build = Build()
        val daoBuild: BuildDAO = BuildDAOSQLImpl(this)
        build.name = "${selectedJobClass.name} | ${selectedJobClass.gameName}"
        build.jobClassName = selectedJobClass.name
        build.gameName = selectedJobClass.gameName
        build.description = "custom build from save function"

        val gson = Gson()
        skillBuildText = gson.toJson(skillData)
        build.skillBuildText = skillBuildText

        daoBuild.addBuild(build)

        Log.d("SKILL LIST FROM SAVE", build.skillBuild.toString())
    }

    override fun showSkillData(skillData: ArrayList<Skill>) {
        for(skill in skillData){
            Log.i("skill data", skill.name)
            Log.i("skill points", skill.currentLevel.toString())
        }
    }

    private fun dialogAddSkill(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAddSkillBinding: DialogAddSkillBinding =
                DialogAddSkillBinding.inflate(LayoutInflater.from(it))

            with(builder) {
                setPositiveButton("Add", DialogInterface.OnClickListener { _, _ ->
                    val dao: SkillDAO = SkillDAOSQLImpl(it)
                    val newSkill = Skill(0,"","","",
                        Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
                        "",10,0,0,"")

                    val addSkillName = dialogAddSkillBinding.editSkillName.text.toString().trim()
                    val addSkillMaxLevel = dialogAddSkillBinding.editSkillMaxLevel.text.toString()

                    if(addSkillName.isNotEmpty() && addSkillMaxLevel.toInt() > 0) {
                        newSkill.name = addSkillName
                        newSkill.maxLevel = addSkillMaxLevel.toInt()
                        newSkill.jobClassName = selectedJobClass.name
                        newSkill.gameName = selectedJobClass.gameName
                        newSkill.minLevel = 0
                        newSkill.currentLevel = 0
                        newSkill.description = "Custom Skill"

                        dao.addSkill(newSkill)
                        Log.i("new class", newSkill.name)
                        Log.i("class game id", selectedJobClass.gameName)
                        Log.i("class id", selectedJobClass.id.toString())
                        Log.i("new skill id", newSkill.id.toString())

                        val newSkills = dao.getSkillPerJob(selectedJobClass.gameName, selectedJobClass.name)

                        Log.i("skill list", newSkills.toString())

                        skillAdapter.updateSkill(newSkills)
                        skillAdapter.notifyItemInserted(newSkill.id)
                        toast("Added ${newSkill.name}.")
                    }else {
                        toast("Error: Class Name is empty or Max Level is below 1.")
                    }


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