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
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.GameAdapter
import com.example.kodegoskillsimulatorapp.dao.GameDAO
import com.example.kodegoskillsimulatorapp.dao.GameDAOSQLImpl
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.JobClassDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivityMainBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddGameBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.model.JobClass
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var dao: GameDAO
    private lateinit var gameAdapter: GameAdapter
    private lateinit var games: ArrayList<Game>
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply{
            title = "Select a Game"
            displayOptions
        }


        dao = GameDAOSQLImpl(applicationContext)
        games = dao.getGames()
        gameAdapter = GameAdapter(games, this, this)
        Log.d("game list", games.toString())
//        binding.gameList.layoutManager = LinearLayoutManager(applicationContext)
        binding.gameList.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.gameList.adapter = gameAdapter


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.skill_simulator_tab -> {
//                    val main = Intent(this, MainActivity::class.java)
//                    startActivity(main)
                    true
                }
                R.id.saved_builds_tab -> {
                    val savedBuilds = Intent(this, SavedBuildsActivity::class.java)
                    startActivity(savedBuilds)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            // Exit the app
            finish()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_game -> {
                dialogAddGame(this)
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

    private fun dialogAddGame(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAddGameBinding: DialogAddGameBinding =
                DialogAddGameBinding.inflate(LayoutInflater.from(it))

            with(builder) {
                setPositiveButton("Add", DialogInterface.OnClickListener { dialog, _ ->
                    val dao: GameDAO = GameDAOSQLImpl(it)
                    val newGame = Game()
                    val addGameName = dialogAddGameBinding.textGameName.text.toString().trim()

                    Log.d("DIALOG ADD GAME", addGameName)

                    if(addGameName.isNotEmpty()) {
                        newGame.name = addGameName
                        dao.addGame(newGame)
                        val newGames = dao.getGames()
                        gameAdapter.updateGame(newGames)
                        gameAdapter.notifyDataSetChanged()
                        toast("Added ${newGame.name}.")
                    }else {
                        toast("Error: Game name is empty.")
                    }
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
                    .setView(dialogAddGameBinding.root)
                    .create()
                    .show()
            }
        }
    }

    private fun toast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


}