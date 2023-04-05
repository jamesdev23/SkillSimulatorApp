package com.example.kodegoskillsimulatorapp.adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.PopupMenuCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.R
import com.example.kodegoskillsimulatorapp.SelectClassActivity
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.dao.GameDAO
import com.example.kodegoskillsimulatorapp.dao.GameDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.DialogAddGameBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogUpdateGameBinding
import com.example.kodegoskillsimulatorapp.databinding.GameItemGridBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.NonDisposableHandle.parent

class GameAdapter (var games: ArrayList<Game>, var activity: Activity, var context: Context)
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

            val popupMenu = PopupMenu(context, itemBinding.btnOptionsRow)

            popupMenu.menuInflater.inflate(R.menu.game_option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.option_edit -> {
                        Snackbar.make(itemBinding.root, "Game name: ${game.name}", Snackbar.LENGTH_SHORT).show()
                        dialogShowUpdateGameInfo(context)
                        true
                    }
                    R.id.option_delete -> {
                        Snackbar.make(itemBinding.root, "Delete ${game.name}", Snackbar.LENGTH_SHORT).show()

                        var dao: GameDAO = GameDAOSQLImpl(itemView.context)
                        bindGame(game)
                        dao.deleteGame(game.id)
                        removeGame(adapterPosition)
                        true
                    }
                    else -> false
                }
            }


            itemBinding.btnOptionsRow.setOnClickListener {
                popupMenu.show()
            }
        }

        override fun onClick(view: View?) {
            // do nothing
        }

        private fun dialogShowUpdateGameInfo(context: Context){
            context.let {
                val builder = android.app.AlertDialog.Builder(it)
                val dialogUpdateGameBinding: DialogUpdateGameBinding =
                    DialogUpdateGameBinding.inflate(LayoutInflater.from(it))

                with(dialogUpdateGameBinding) {
                    textGameName.setText(game.name)
                    textGameDescription.setText(game.description)
                }

                with(builder) {
                    setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
                        val dao: GameDAO = GameDAOSQLImpl(it)
                        val updateGameName =
                            dialogUpdateGameBinding.textGameName.text.toString()
                        val updateGameDescription =
                            dialogUpdateGameBinding.textGameDescription.text.toString()

                        game.name = updateGameName
                        game.description = updateGameDescription

                        dao.updateGame(game.id, game)
                        updateGame(dao.getGames())
                        notifyItemChanged(adapterPosition)
                        Snackbar.make(itemBinding.root, "Updated ${game.name}", Snackbar.LENGTH_SHORT).show()
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                        // Do something
                    })
                        .setView(dialogUpdateGameBinding.root)
                        .create()
                        .show()
                }
            }
        }
    }


}