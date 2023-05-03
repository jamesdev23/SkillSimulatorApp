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
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.R
import com.example.kodegoskillsimulatorapp.SelectClassActivity
import com.example.kodegoskillsimulatorapp.dao.GameDAO
import com.example.kodegoskillsimulatorapp.dao.GameDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.DialogEditGameBinding
import com.example.kodegoskillsimulatorapp.databinding.ItemGameGridBinding
import com.example.kodegoskillsimulatorapp.model.Game
import com.google.android.material.snackbar.Snackbar

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

        val itemBinding = ItemGameGridBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return GameViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GameViewHolder,
                                  position: Int) {
        holder.bindGame(games[position])
    }

    inner class GameViewHolder(private val itemBinding: ItemGameGridBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var game = Game()

        init{
            itemView.setOnClickListener(this)
        }

        fun bindGame(game: Game) {
            this.game = game

            itemBinding.gameName.text = game.name

            itemBinding.gamePicture.setImageBitmap(game.icon)
            
            itemBinding.btnOptionsRow.setOnClickListener {
                popupMenu()
            }
        }

        override fun onClick(view: View?) {
            val intent = Intent(view?.context, SelectClassActivity::class.java)

            val bundle = Bundle()
            bundle.putString("DATA_GAME_NAME", games[position].name)
            intent.putExtras(bundle)

            view?.context?.startActivity(intent)
        }

        private fun popupMenu() {
            val popupMenu = PopupMenu(context, itemBinding.btnOptionsRow)

            popupMenu.menuInflater.inflate(R.menu.item_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.option_edit -> {
                        Snackbar.make(itemBinding.root, "Game name: ${game.name}", Snackbar.LENGTH_SHORT).show()
                        dialogEditGameInfo(context)
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

            popupMenu.show()
        }

        private fun dialogEditGameInfo(context: Context){
            context.let {
                val builder = android.app.AlertDialog.Builder(it)
                val dialogEditGameBinding: DialogEditGameBinding =
                    DialogEditGameBinding.inflate(LayoutInflater.from(it))

                with(dialogEditGameBinding) {
                    editGameName.setText(game.name)
                    editGameDescription.setText(game.description)
                }

                with(builder) {
                    setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
                        val dao: GameDAO = GameDAOSQLImpl(it)
                        val editGameName =
                            dialogEditGameBinding.editGameName.text.toString()
                        val editGameDescription =
                            dialogEditGameBinding.editGameDescription.text.toString()

                        game.name = editGameName
                        game.description = editGameDescription

                        dao.updateGame(game.id, game)
                        updateGame(dao.getGames())
                        notifyItemChanged(adapterPosition)
                        Snackbar.make(itemBinding.root, "Updated ${game.name}", Snackbar.LENGTH_SHORT).show()
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                        // Do something
                    })
                        .setView(dialogEditGameBinding.root)
                        .create()
                        .show()
                }
            }
        }
    }


}