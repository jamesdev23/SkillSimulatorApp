package com.example.kodegoskillsimulatorapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.kodegoskillsimulatorapp.model.Game

interface GameDAO {
    fun addGame(game: Game)
    fun getGames(): ArrayList<Game>
    fun updateGame(gameId: Int, game: Game)
    fun deleteGame(gameId: Int)
    fun getImage(cursor: Cursor, game: Game)
}

class GameDAOSQLImpl(var context: Context): GameDAO {
    override fun addGame(game: Game){
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.gameName, game.name)
        contentValues.put(DatabaseHandler.gameIcon, game.icon)

        val success = db.insert(DatabaseHandler.tableSkills,null,contentValues)
        db.close()
    }


    override fun getGames(): ArrayList<Game> {
        val gameList: ArrayList<Game> = ArrayList()

        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.gameId,
            DatabaseHandler.gameName,
            DatabaseHandler.gameIcon
        )

        try{
            cursor = db.query(
                DatabaseHandler.tableGames,
                columns,
                null,
                null,
                null,
                null,
                null
            )

        } catch (e: SQLiteException) {
            db.close()
            return ArrayList()
        }

        var game = Game()
        if(cursor.moveToFirst()) {
            do {
                game = Game()
                game.id = cursor.getInt(0)
                game.name = cursor.getString(1)

                // calls function to get image and process it to bitmap
                getImage(cursor,game)

                gameList.add(game)

            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return gameList
    }

    override fun updateGame(gameId: Int, game: Game) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.gameName, game.name)
//        contentValues.put(DatabaseHandler.gameIcon, game.icon)

        val values = arrayOf("$gameId")
        val success = db.update(
            DatabaseHandler.tableGames,
            contentValues,
            "${DatabaseHandler.gameId} = ?",
            values)
        db.close()
    }

    override fun deleteGame(gameId: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val values = arrayOf("$gameId")
        val success = db.delete(
            DatabaseHandler.tableGames,
            "${DatabaseHandler.gameId} = ?",
            values)
        db.close()
    }

    override fun getImage(cursor: Cursor, game: Game) {
        val bitmap: Bitmap

        try {
            val imageBase64:String = cursor.getString(3)
            val imageByteArray:ByteArray = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT)

            bitmap = imageByteArray.let { BitmapFactory.decodeByteArray(it, 0, it.size) }!!
            game.icon = bitmap.toString()
        }catch (e:Exception){
            Log.e("Error", "image text is bad/empty or image bytearray is null",e)
        }
    }
}