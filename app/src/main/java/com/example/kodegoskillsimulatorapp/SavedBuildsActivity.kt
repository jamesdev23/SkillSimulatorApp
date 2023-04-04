package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.BuildAdapter
import com.example.kodegoskillsimulatorapp.adapter.JobClassAdapter
import com.example.kodegoskillsimulatorapp.dao.BuildDAO
import com.example.kodegoskillsimulatorapp.dao.BuildDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySavedBuildsBinding
import com.example.kodegoskillsimulatorapp.model.Build
import com.example.kodegoskillsimulatorapp.model.Skill

class SavedBuildsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBuildsBinding

    private lateinit var dao: BuildDAO
    private lateinit var buildAdapter: BuildAdapter
    private lateinit var builds: ArrayList<Build>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBuildsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Saved Builds"

        dao = BuildDAOSQLImpl(this)
        builds = dao.getBuilds()
        buildAdapter = BuildAdapter(builds, this)
        binding.savedBuildsList.layoutManager = LinearLayoutManager(applicationContext)
        binding.savedBuildsList.adapter = buildAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.saved_builds_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_select_game -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.action_select_class -> {
                val intent = Intent(this, SelectClassActivity::class.java)
                intent.putExtra("data", "Ragnarok Online (iRO)")
                startActivity(intent)
                finish()
                return true
            }
            R.id.action_add_build -> {
                val intent = Intent(this, SkillListActivity::class.java)
                intent.putExtra("data2", "Ragnarok Online (iRO)")
                intent.putExtra("data3", "Knight")
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}