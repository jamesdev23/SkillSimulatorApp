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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Skill Simulator - Select a Game"
        supportActionBar?.displayOptions

        dao = GameDAOSQLImpl(applicationContext)
        games = dao.getGames()
        gameAdapter = GameAdapter(games, this)
        Log.d("game list", games.toString())
//        binding.gameList.layoutManager = LinearLayoutManager(applicationContext)
        binding.gameList.layoutManager = GridLayoutManager(applicationContext, 3)
        binding.gameList.adapter = gameAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
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
                setPositiveButton("Add", DialogInterface.OnClickListener { _, _ ->
                    val dao: GameDAO = GameDAOSQLImpl(it)
                    val newGame = Game()

                    val addGameName =
                        dialogAddGameBinding.textGameName.text.toString()

                    newGame.name = addGameName

                    dao.addGame(newGame)
                    gameAdapter.updateGame(dao.getGames())
                    gameAdapter.notifyDataSetChanged()
                    Snackbar.make(binding.root, "Added new game.", Snackbar.LENGTH_SHORT).show()
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    // Do something when user press the positive button
                })
                    .setView(dialogAddGameBinding.root)
                    .create()
                    .show()
            }
        }
    }


}