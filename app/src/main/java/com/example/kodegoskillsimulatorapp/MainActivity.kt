package com.example.kodegoskillsimulatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.kodegoskillsimulatorapp.dao.GameDAO
import com.example.kodegoskillsimulatorapp.dao.GameDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ActivityMainBinding
import com.example.kodegoskillsimulatorapp.model.Game

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: GameDAO
    private var games: ArrayList<Game> = ArrayList()
    private var gameNames: ArrayList<String> = ArrayList()
    private var gameSelected: String = ""
    private var gameSelectedId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        dao = GameDAOSQLImpl(applicationContext)
        games = dao.getGames()
        for (game in games) {
            gameNames.add(game.name)
        }

        val gameListAdapter = ArrayAdapter<String>(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gameNames)
        gameListAdapter.setDropDownViewResource(
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerSelectGame.adapter = gameListAdapter


        binding.spinnerSelectGame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                gameSelectedId = position
                Log.i("game_selected", gameSelected)
                Log.i("game_id", position.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to handle nothing selected
            }
        }



        binding.btnContinue.setOnClickListener{
            val goToNextActivity = Intent(this, SelectClassActivity::class.java)
            goToNextActivity.putExtra("data_game_name", gameSelected)
            goToNextActivity.putExtra("data_game_id", gameSelectedId)
            startActivity(goToNextActivity)
            finish()
        }
    }
}