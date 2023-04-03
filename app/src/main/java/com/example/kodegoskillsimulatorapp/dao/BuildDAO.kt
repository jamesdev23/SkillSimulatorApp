package com.example.kodegoskillsimulatorapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.example.kodegoskillsimulatorapp.model.Build
import com.example.kodegoskillsimulatorapp.model.Skill
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface BuildDAO {
    fun addBuild(build: Build)
    fun getBuilds(): ArrayList<Build>
    fun deleteBuild(buildID: Int)
}

class BuildDAOSQLImpl(var context: Context): BuildDAO {

    override fun addBuild(build: Build) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.buildJobClassName, build.jobClassName)
        contentValues.put(DatabaseHandler.buildGameName, build.gameName)
        contentValues.put(DatabaseHandler.buildName, build.name)
        contentValues.put(DatabaseHandler.buildDescription, build.description)
        contentValues.put(DatabaseHandler.buildDataText, build.dataText)

        val success = db.insert(DatabaseHandler.tableBuilds,null,contentValues)
        db.close()
    }


    override fun getBuilds(): ArrayList<Build> {
        val buildList: ArrayList<Build> = ArrayList()
        var skillDataJson = ""
        val gson = Gson()

        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.buildId,
            DatabaseHandler.buildJobClassName,
            DatabaseHandler.buildGameName,
            DatabaseHandler.buildName,
            DatabaseHandler.buildDescription,
            DatabaseHandler.buildDataText
        )

        try{
            cursor = db.query(
                DatabaseHandler.tableBuilds,
                columns,
                null,
                null,
                null,
                null,
                DatabaseHandler.buildId
            )

        } catch (e: SQLiteException) {
            db.close()
            return ArrayList()
        }

        var build = Build()
        if(cursor.moveToFirst()) {
            do {
                build = Build()
                build.id = cursor.getInt(0)
                build.jobClassName = cursor.getString(1)
                build.gameName = cursor.getString(2)
                build.name = cursor.getString(3)
                build.description = cursor.getString(4)

                // converts json back to ArrayList<Skill>
                skillDataJson = cursor.getString(5)
                build.dataText = skillDataJson
                build.data = gson.fromJson(skillDataJson, object : TypeToken<ArrayList<Skill>>() {}.type)

                buildList.add(build)

            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return buildList
    }

    override fun deleteBuild(buildID: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val values = arrayOf(buildID.toString())
        val success = db.delete(
            DatabaseHandler.tableBuilds,
            "${DatabaseHandler.buildId} = ?",
            values)
        db.close()
    }
}