package com.fauzan.githubuserapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.fauzan.githubuserapp.database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.fauzan.githubuserapp.database.DatabaseContract.FavoriteColumns.Companion.HTML
import com.fauzan.githubuserapp.database.DatabaseContract.FavoriteColumns.Companion.ID
import com.fauzan.githubuserapp.database.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.fauzan.githubuserapp.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.fauzan.githubuserapp.model.ResponseDetailUser

class FavoriteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase
    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): ArrayList<ResponseDetailUser> {
        val arrayList = ArrayList<ResponseDetailUser>()
        if (openDatabase()) {
            val cursor = database.query(
                DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                "$ID ASC",
                null
            )
            cursor.moveToFirst()
            var listFav: ResponseDetailUser
            if (cursor.count > 0) {
                do {
                    listFav = ResponseDetailUser()
                    listFav.id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    listFav.avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR))
                    listFav.htmlUrl = cursor.getString(cursor.getColumnIndexOrThrow(HTML))
                    listFav.login = cursor.getString(cursor.getColumnIndexOrThrow(LOGIN))
                    arrayList.add(listFav)
                    cursor.moveToNext()
                } while (!cursor.isAfterLast)
            }
            cursor.close()
            closeDatabase()
        }
        return arrayList
    }

    fun insert(response: ResponseDetailUser): Long {
        if (openDatabase()) {
            val values = ContentValues()
            values.put(ID, response.id)
            values.put(AVATAR, response.avatarUrl)
            values.put(HTML, response.htmlUrl)
            values.put(LOGIN, response.login)
            val result = database.insert(DATABASE_TABLE, null, values)
            closeDatabase()
            return result
        }
        return -1L
    }

    fun delete(username: String): Int {
        if (openDatabase()) {
            val result = database.delete(TABLE_NAME, "$LOGIN = '$username'", null)
            closeDatabase()
            return result
        }
        return -1
    }

    private fun openDatabase(): Boolean {
        if (!database.isOpen) {
            database = databaseHelper.writableDatabase
        }
        return database.isOpen
    }

    private fun closeDatabase() {
        if (database.isOpen) {
            database.close()
        }
    }

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }
}