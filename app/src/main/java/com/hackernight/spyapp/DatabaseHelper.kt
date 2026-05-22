package com.hackernight.spyapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "spy_queue.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "message_queue"
        const val COL_ID = "id"
        const val COL_MESSAGE = "message"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_MESSAGE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addMessage(message: String) {
        writableDatabase.execSQL("INSERT INTO $TABLE_NAME ($COL_MESSAGE) VALUES (?)", arrayOf(message))
    }

    fun getAllMessages(): List<String> {
        val list = mutableListOf<String>()
        val cursor = readableDatabase.rawQuery("SELECT $COL_MESSAGE FROM $TABLE_NAME ORDER BY $COL_ID ASC", null)
        while (cursor.moveToNext()) list.add(cursor.getString(0))
        cursor.close()
        return list
    }

    fun deleteAll() {
        writableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }
}
