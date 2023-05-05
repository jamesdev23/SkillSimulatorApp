package com.example.kodegoskillsimulatorapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.BuildAdapter
import com.example.kodegoskillsimulatorapp.dao.BuildDAO
import com.example.kodegoskillsimulatorapp.dao.BuildDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivitySavedBuildsBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddBuildBinding
import com.example.kodegoskillsimulatorapp.model.Build

class SavedBuildsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBuildsBinding

    private lateinit var dao: BuildDAO
    private lateinit var buildAdapter: BuildAdapter
    private lateinit var builds: ArrayList<Build>
    private var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBuildsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply{
            title = "Saved Builds"
            setDisplayHomeAsUpEnabled(true)
            displayOptions
        }

        dao = BuildDAOSQLImpl(this)
        builds = dao.getBuilds()
        buildAdapter = BuildAdapter(builds, this)
        binding.savedBuildsList.layoutManager = LinearLayoutManager(applicationContext)
        binding.savedBuildsList.adapter = buildAdapter

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.skill_simulator_tab -> {
                    val main = Intent(this, MainActivity::class.java)
                    startActivity(main)
                    true
                }
                R.id.saved_builds_tab -> {
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
        menuInflater.inflate(R.menu.saved_builds_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_build -> {
                dialogAddBuild(this)
                return true
            }
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
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun dialogAddBuild(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAddBuildBinding: DialogAddBuildBinding =
                DialogAddBuildBinding.inflate(LayoutInflater.from(it))

            with(builder) {
                setPositiveButton("Add", DialogInterface.OnClickListener { _, _ ->
                    val dao: BuildDAO = BuildDAOSQLImpl(it)
                    val newBuild = Build()

                    val addBuildName = dialogAddBuildBinding.editBuildName.text.toString().trim()


                    val buildSearch = dao.getBuildByName(addBuildName)

                    when {
                        buildSearch.name.isNotEmpty() -> toast("Error: Duplicate name.")
                        addBuildName.isEmpty() -> toast("Error: Build name is empty.")
                        addBuildName.length > 200 -> toast("Error: Name exceeds 200 characters")
                        else -> {
                            newBuild.name = addBuildName
                            newBuild.description = "Custom Build"
                            newBuild.gameName = "Custom Game"
                            newBuild.jobClassName = "Custom Class"
                            newBuild.skillBuild = arrayListOf()

                            Log.i("add build name", newBuild.name)

                            dao.addBuild(newBuild)

                            var newBuilds = dao.getBuilds()
                            Log.i("build list", newBuilds.toString())
                            buildAdapter.updateBuild(newBuilds)
                            buildAdapter.notifyDataSetChanged()

                            toast("Added ${newBuild.name}")
                        }
                    }
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    // Do something when user press the positive button
                })
                    .setView(dialogAddBuildBinding.root)
                    .create()
                    .show()
            }
        }
    }

    private fun toast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}