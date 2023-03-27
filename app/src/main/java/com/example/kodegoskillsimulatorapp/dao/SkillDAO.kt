package com.example.kodegoskillsimulatorapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.kodegoskillsimulatorapp.model.Skill

interface SkillDAO {
    fun addSkill(skill: Skill)
    fun getSkills(): ArrayList<Skill>
    fun getSkillPerJob(jobclassID: Int): ArrayList<Skill>
    fun updateSkill(skillId: Int, skill: Skill)
    fun deleteSkill(skillId: Int)

    fun getIcon(cursor: Cursor, skill: Skill)
}

class SkillDAOSQLImpl(var context: Context): SkillDAO {
    override fun addSkill(skill: Skill) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.skillName, skill.name)
        contentValues.put(DatabaseHandler.skillMaxLevel, skill.level)

        val success = db.insert(DatabaseHandler.tableSkills,null,contentValues)
        db.close()
    }

    override fun getSkills(): ArrayList<Skill> {
        val skillList: ArrayList<Skill> = ArrayList()

        val columns = arrayOf(
            DatabaseHandler.skillId,
            DatabaseHandler.skillName,
            DatabaseHandler.skillMaxLevel
        )

        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                DatabaseHandler.tableSkills,
                columns,
                null,
                null,
                null,
                null,
            DatabaseHandler.skillId
            )
        } catch (e: SQLiteException) {
            db.close()
            return ArrayList()
        }

        var skill = Skill()
        if(cursor.moveToFirst()) {
            do {
                skill = Skill()
                skill.id = cursor.getInt(2)
                skill.name = cursor.getString(0)
                skill.level = cursor.getInt(1)

                skillList.add(skill)
            }while(cursor.moveToNext())
        }

        cursor?.close()
        db.close()
        return skillList
    }

    override fun getSkillPerJob(jobclassID: Int): ArrayList<Skill> {
        val skillList: ArrayList<Skill> = ArrayList()

        // setting up jobclass id since it gave position instead of actual jobclass id that starts with 1 (one)
        val adjustment = jobclassID.toInt().plus(1)
        val newJobclassID = adjustment.toString()

        val columns = arrayOf(
            DatabaseHandler.skillId,
            DatabaseHandler.skillJobclassID,
            DatabaseHandler.skillName,
            DatabaseHandler.skillMaxLevel,
            DatabaseHandler.skillIcon
        )

        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.query(
                DatabaseHandler.tableSkills,
                columns,
                "${DatabaseHandler.skillJobclassID} = ?",
                arrayOf(newJobclassID),
                null,
                null,
                DatabaseHandler.skillId
            )
        } catch (e: SQLiteException) {
            db.close()
            return ArrayList()
        }

        var skill = Skill()
        if(cursor.moveToFirst()) {
            do {
                skill = Skill()
                skill.id = cursor.getInt(0)
                skill.jobClassSkillID = cursor.getInt(1)
                skill.name = cursor.getString(2)
                skill.level = cursor.getInt(3)
                getIcon(cursor,skill)

                skillList.add(skill)
            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return skillList
    }

    override fun updateSkill(skillId: Int, skill: Skill) {
        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.skillName, skill.name)
        contentValues.put(DatabaseHandler.skillMaxLevel, skill.level)

        val values = arrayOf("$skillId")
        val success = db.update(
            DatabaseHandler.tableSkills,
            contentValues,
            "${DatabaseHandler.skillId} = ?",
            values)
        db.close()
    }

    override fun deleteSkill(skillId: Int) {
        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val values = arrayOf("$skillId")
        val success = db.delete(
            DatabaseHandler.tableSkills,
            "${DatabaseHandler.skillId} = ?",
            values)
        db.close()
    }

    override fun getIcon(cursor: Cursor, skill: Skill) {
        var bitmap: Bitmap

        try {
            val iconBase64:String = cursor.getString(4)
            val iconByteArray:ByteArray = android.util.Base64.decode(iconBase64, android.util.Base64.DEFAULT)

            bitmap = iconByteArray.let { BitmapFactory.decodeByteArray(it, 0, it.size) }!!
            skill.icon = bitmap
        }catch (e:Exception){
            Log.e("Error", "icon text is bad/empty or icon bytearray is null",e)
        }
    }
}