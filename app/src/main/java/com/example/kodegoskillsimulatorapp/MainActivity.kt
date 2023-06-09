package com.example.kodegoskillsimulatorapp

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kodegoskillsimulatorapp.adapter.GameAdapter
import com.example.kodegoskillsimulatorapp.dao.GameDAO
import com.example.kodegoskillsimulatorapp.dao.GameDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivityMainBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAboutBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogAddGameBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.utils.ImageUploadUtility


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var dao: GameDAO
    private lateinit var gameAdapter: GameAdapter
    private lateinit var games: ArrayList<Game>
    private var backPressedTime: Long = 0
    private var iconText: String = ""

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
            R.id.action_about -> {
                dialogAbout(this)
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

                    val gameSearch = dao.getGameByName(addGameName)

                    when {
                        gameSearch.name.isNotEmpty() -> toast("Error: Duplicate name.")
                        addGameName.isEmpty() -> toast("Error: Game name is empty.")
                        addGameName.length > 200 -> toast("Error: Name exceeds 200 characters")
                        else -> {
                            newGame.name = addGameName
                            newGame.description = "Custom game"

                            dao.addGame(newGame)
                            val newGameList = dao.getGames()
                            gameAdapter.updateGame(newGameList)
                            gameAdapter.notifyDataSetChanged()
                            toast("Added ${newGame.name}.")
                        }
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

    private fun dialogAbout(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAboutBinding: DialogAboutBinding =
                DialogAboutBinding.inflate(LayoutInflater.from(it))

            with(builder) {
                setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                    .setView(dialogAboutBinding.root)
                    .create()
                    .show()
            }
        }
    }

    private fun toast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


}