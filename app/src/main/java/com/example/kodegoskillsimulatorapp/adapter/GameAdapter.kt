package com.example.kodegoskillsimulatorapp.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.SelectClassActivity
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.dao.GameDAO
import com.example.kodegoskillsimulatorapp.dao.GameDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.GameItemGridBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.google.android.material.snackbar.Snackbar

class GameAdapter (var games: ArrayList<Game>, var activity: Activity)
    : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    fun addGame(game: Game){
        games.add(0,game)
        notifyItemInserted(0)
    }

    fun removeGame(position: Int){
        games.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateGame(newGame: ArrayList<Game>){
        games.clear()
        games.addAll(newGame)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return games.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameViewHolder {

        val itemBinding = GameItemGridBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return GameViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GameViewHolder,
                                  position: Int) {
        holder.bindGame(games[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(activity.applicationContext, SelectClassActivity::class.java)

            val bundle = Bundle()
            bundle.putString("data", games[position].name)
            intent.putExtras(bundle)

            activity.startActivity(intent)
            activity.finish()
        }
    }

    inner class GameViewHolder(private val itemBinding: GameItemGridBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var game = Game()

        init{
            itemView.setOnClickListener(this)
        }

        fun bindGame(game: Game) {
            this.game = game

            itemBinding.gameName.text = "${game.name}"
            itemBinding.gamePicture.setImageBitmap(game.icon)

//            itemBinding.btnOptionsRow.setOnClickListener {
//                Snackbar.make(
//                    itemBinding.root,
//                    "Delete by button",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//
//                var dao: GameDAO = GameDAOSQLImpl(it.context)
//                bindGame(game)
//                dao.deleteGame(game.id)
//                removeGame(adapterPosition)
//            }
        }

        override fun onClick(view: View?) {
            // do nothing
        }
    }
}