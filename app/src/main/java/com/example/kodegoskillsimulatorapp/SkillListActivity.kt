package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.SkillAdapter
import com.example.kodegoskillsimulatorapp.dao.SkillDAO
import com.example.kodegoskillsimulatorapp.dao.SkillDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySkillListBinding
import com.example.kodegoskillsimulatorapp.model.Skill

class SkillListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySkillListBinding
    private lateinit var skillAdapter: SkillAdapter
    private lateinit var dao: SkillDAO
    private var skills: ArrayList<Skill> = ArrayList()
    private val maxSkillPoints = 49
    private var jobclassData: Int = 0
    private var jobclassName: String = "Knight"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkillListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        jobclassData =  bundle!!.getInt("item_position",0)
        jobclassName =  bundle!!.getString("jobclass_name","Knight")

        supportActionBar?.title = jobclassName

//        binding.skillpointsTotal.text = " / ${maxSkillPoints.toString()}"

        dao = SkillDAOSQLImpl(applicationContext)
        skillSimulatorSetup(dao, jobclassData)

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.reset_skill_menu -> {
//                resetSkillPoints(jobclassData)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun skillSimulatorSetup(dao: SkillDAO, jobclassData: Int) {
        skills = jobclassData.let { dao.getSkillPerJob(it) }!!
        skillAdapter = SkillAdapter(skills, this)
//        binding.skillList.layoutManager = GridLayoutManager(applicationContext,2)
        binding.skillList.layoutManager = LinearLayoutManager(applicationContext)
        binding.skillList.adapter = skillAdapter
//        setSkillPointsLabelToDefault()
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