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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.SkillAdapter
import com.example.kodegoskillsimulatorapp.dao.SkillDAO
import com.example.kodegoskillsimulatorapp.dao.SkillDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySkillListBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddSkillBinding
import com.example.kodegoskillsimulatorapp.model.JobClass
import com.example.kodegoskillsimulatorapp.model.Skill

class SkillListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySkillListBinding
    private lateinit var skillAdapter: SkillAdapter
    private lateinit var dao: SkillDAO
    private lateinit var skills: ArrayList<Skill>
    private val maxSkillPoints = 49
    private var jobClassSelected: JobClass = JobClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkillListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobClassSelected.id = intent.extras?.getString("data3")?.toInt() ?: 0
        jobClassSelected.gameId = intent.extras?.getString("data4")?.toInt() ?: 0
        jobClassSelected.name = intent.extras?.getString("data5").toString()
        Log.i("skilllist class id", jobClassSelected.id.toString())
        Log.i("skilllist game id", jobClassSelected.gameId.toString())
        Log.i("skilllist class name", jobClassSelected.name)

        supportActionBar?.title =jobClassSelected.name

//        binding.skillpointsTotal.text = " / ${maxSkillPoints.toString()}"

        dao = SkillDAOSQLImpl(applicationContext)
        skills = dao.getSkillPerJob(jobClassSelected.gameId, jobClassSelected.id)
        skillAdapter = SkillAdapter(skills, this)
        binding.skillList.layoutManager = LinearLayoutManager(applicationContext)
        binding.skillList.adapter = skillAdapter
//        setSkillPointsLabelToDefault()

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
            else -> return super.onOptionsItemSelected(item)
        }
    }
    
    

//    override fun getTotalProgress(list:MutableList<Int>) {
//        val totalSP = list.sum().toString()
//        val skillPointsTextView = findViewById<TextView>(R.id.skillpoints_current)
//        skillPointsTextView.text = totalSP
//    }

//    override fun checkSkillPoints(list: MutableList<Int>) {
//        var newProgressTotal = list.sum()
//        if(newProgressTotal <= maxSkillPoints) {
//            setSkillPointsLabelToDefault()
//        } else {
//            binding.skillpointsLabel.setTextColor(getColor(R.color.fire_engine_red))
//            binding.skillpointsCurrent.setTextColor(getColor(R.color.fire_engine_red))
//            binding.skillpointsTotal.setTextColor(getColor(R.color.fire_engine_red))
//        }
//    }

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

                    newSkill.jobClassId = jobClassSelected.id
                    newSkill.gameId = jobClassSelected.gameId
                    newSkill.name = addSkillName
                    newSkill.maxLevel = addSkillMaxLevel.toInt()
                    newSkill.minLevel = addSkillMinLevel.toInt()
                    newSkill.description = addSkillDescription

                    dao.addSkill(newSkill)
                    Log.i("new class", newSkill.name)
                    Log.i("class game id", jobClassSelected.gameId.toString())
                    Log.i("class id", jobClassSelected.id.toString())

                    var newSkills = dao.getSkillPerJob(jobClassSelected.gameId, jobClassSelected.id)
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


    override fun onBackPressed() {
        super.onBackPressed()
        val goToSelect = Intent(this,SelectClassActivity::class.java)
        startActivity(goToSelect)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}