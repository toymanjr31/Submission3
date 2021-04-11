package com.dicoding.androidprogramming.submission3.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import java.sql.SQLException

class FavHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase = databaseHelper.writableDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavHelper? = null
        fun getInstance(context: Context): FavHelper = 
            INSTANCE?: synchronized(this){
                INSTANCE?: FavHelper(context)
            }

    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()
        if (database.isOpen){
            database.close()
        }
    }

    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$USERNAME ASC"
        )
    }

    fun queryByID(id: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int{
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }

    fun deleteByID(id: String): Int{
        return database.delete(DATABASE_TABLE, "$USERNAME = $id", null)
    }
}