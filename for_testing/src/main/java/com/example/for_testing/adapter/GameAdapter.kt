package com.example.for_testing.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.for_testing.databinding.GameItemBinding
import com.example.for_testing.dao.GameDAO
import com.example.for_testing.dao.GameDAOSQLImpl
import com.example.for_testing.model.Game
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

        val itemBinding = GameItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return GameViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GameViewHolder,
                                  position: Int) {
        holder.bindGame(games[position])

//        holder.itemView.setOnClickListener {
//            val intent = Intent(activity.applicationContext, SelectClassActivity::class.java)
//
//            val bundle = Bundle()
//            bundle.putInt("data_game_id", position)
//            bundle.putString("data_game_name", games[position].name)
//            intent.putExtras(bundle)
//
//            activity.startActivity(intent)
//        }
    }

    inner class GameViewHolder(private val itemBinding: GameItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var game = Game()

        init{
            itemView.setOnClickListener(this)
        }

        fun bindGame(game: Game) {
            this.game = game

            itemBinding.gameName.text = "${game.name}"
            itemBinding.gamePicture.setImageBitmap(game.icon)

            itemBinding.btnOptionsRow.setOnClickListener {
                Snackbar.make(
                    itemBinding.root,
                    "Delete by button",
                    Snackbar.LENGTH_SHORT
                ).show()

                var dao: GameDAO = GameDAOSQLImpl(it.context)
                bindGame(game)
                dao.deleteGame(game.id)
                removeGame(adapterPosition)
            }
        }

        override fun onClick(view: View?) {
            // do nothing
        }
    }
}