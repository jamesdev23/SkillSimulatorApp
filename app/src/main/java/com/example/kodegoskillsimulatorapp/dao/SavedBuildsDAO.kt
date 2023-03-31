package com.example.kodegoskillsimulatorapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.model.SavedBuild
import com.example.kodegoskillsimulatorapp.model.Skill
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface SavedBuildsDAO {
    fun addSavedBuild(savedBuild: SavedBuild)
    fun getSavedBuilds(): ArrayList<SavedBuild>
    fun deleteSavedBuild(savedBuildID: Int)
}

class SavedBuildsDAOSQLImpl(var context: Context): SavedBuildsDAO {

    override fun addSavedBuild(savedBuild: SavedBuild) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.savedBuildsSaveData, savedBuild.saveData)

        val success = db.insert(DatabaseHandler.tableSavedBuilds,null,contentValues)
        db.close()
    }


    override fun getSavedBuilds(): ArrayList<SavedBuild> {
        val savedBuildList: ArrayList<SavedBuild> = ArrayList()
        var skillDataJson = ""
        val gson = Gson()

        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.savedBuildsId,
            DatabaseHandler.savedBuildsSaveData
        )

        try{
            cursor = db.query(
                DatabaseHandler.tableSavedBuilds,
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

        var savedBuild = SavedBuild()
        if(cursor.moveToFirst()) {
            do {
                savedBuild = SavedBuild()
                savedBuild.id = cursor.getInt(0)
                skillDataJson = cursor.getString(1)

                savedBuild.saveData = skillDataJson
                savedBuild.skillData = gson.fromJson(skillDataJson, object : TypeToken<ArrayList<Skill>>() {}.type)

                savedBuildList.add(savedBuild)

            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return savedBuildList
    }

    override fun deleteSavedBuild(savedBuildID: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val values = arrayOf(savedBuildID.toString())
        val success = db.delete(
            DatabaseHandler.tableSavedBuilds,
            "${DatabaseHandler.savedBuildsId} = ?",
            values)
        db.close()
    }
}