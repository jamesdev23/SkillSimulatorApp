package com.example.kodegoskillsimulatorapp

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var dao: GameDAO
    private lateinit var gameAdapter: GameAdapter
    private var games: ArrayList<Game> = ArrayList()
    private var gameNames: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        dao = GameDAOSQLImpl(applicationContext)
        games = dao.getGames()
        gameAdapter = GameAdapter(games, this)
        Log.d("game list", games.toString())
        binding.gameList.layoutManager = LinearLayoutManager(applicationContext)
        binding.gameList.adapter = gameAdapter

        binding.btnAddGame.setOnClickListener {
            dialogAddGame(it.context)
        }

    }

    private fun dialogAddGame(context: Context){
        context.let {
            val builder = android.app.AlertDialog.Builder(it)
            val dialogAddGameBinding: DialogAddGameBinding =
                DialogAddGameBinding.inflate(LayoutInflater.from(it))

            with(dialogAddGameBinding) {
                textGameName.setText("Custom Game")
            }

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