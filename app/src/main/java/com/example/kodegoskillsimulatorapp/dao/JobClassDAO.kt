package com.example.kodegoskillsimulatorapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.kodegoskillsimulatorapp.model.JobClass

interface JobClassDAO {
    fun addJobclass(jobClass: JobClass)
    fun getJobclassPerGame(gameId: Int): ArrayList<JobClass>
    fun updateJobClass(jobClassId: Int, jobClass: JobClass)
    fun deleteJobClass(jobClassId: Int)
    fun getImage(cursor: Cursor, jobClass: JobClass)
}

class JobClassDAOSQLImpl(var context: Context): JobClassDAO {
    override fun addJobclass(jobClass: JobClass) {
        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.jobclassName, jobClass.name)
        contentValues.put(DatabaseHandler.jobclassType, jobClass.type)

        val success = db.insert(DatabaseHandler.tableJobclasses,null,contentValues)
        db.close()
    }

    override fun getJobclassPerGame(gameId: Int): ArrayList<JobClass> {
        val jobClassList: ArrayList<JobClass> = ArrayList()

        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.jobclassId,
            DatabaseHandler.jobclassName,
            DatabaseHandler.jobclassType,
            DatabaseHandler.jobclassDescription,
            DatabaseHandler.jobclassImage
        )

        try{
            val values = arrayOf("$gameId")
            cursor = db.query(
                DatabaseHandler.tableJobclasses,
                columns,
                "${DatabaseHandler.classGameId} = ?",
                values,
                null,
                null,
                null
            )

        } catch (e: SQLiteException) {
            db.close()
            return ArrayList()
        }

        var jobClass = JobClass()
        if(cursor.moveToFirst()) {
            do {
                jobClass = JobClass()
                jobClass.id = cursor.getInt(0)
                jobClass.name = cursor.getString(1)
                jobClass.type = cursor.getString(2)
                jobClass.description = cursor.getString(3)

                // calls function to get image and process it to bitmap
                getImage(cursor,jobClass)

                jobClassList.add(jobClass)

            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return jobClassList
    }

    override fun updateJobClass(jobClassId: Int, jobClass: JobClass) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.jobclassName, jobClass.name)
        contentValues.put(DatabaseHandler.jobclassType, jobClass.type)

        val values = arrayOf("$jobClassId")
        val success = db.update(
            DatabaseHandler.tableJobclasses,
            contentValues,
            "${DatabaseHandler.jobclassId} = ?",
            values)
        db.close()
    }

    override fun deleteJobClass(jobClassId: Int) {
        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val values = arrayOf("$jobClassId")
        val success = db.delete(
            DatabaseHandler.tableJobclasses,
            "${DatabaseHandler.jobclassId} = ?",
            values)
        db.close()
    }

    override fun getImage(cursor: Cursor, jobClass: JobClass) {
        var bitmap:Bitmap

        try {
            val imageBase64:String = cursor.getString(3)
            val imageByteArray:ByteArray = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT)

            bitmap = imageByteArray.let { BitmapFactory.decodeByteArray(it, 0, it.size) }!!
            jobClass.img = bitmap
        }catch (e:Exception){
            Log.e("Error", "image text is bad/empty or image bytearray is null",e)
        }
    }


}