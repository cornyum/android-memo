package com.cornming.memo.util

import android.content.ContentValues
import android.database.Cursor
import com.cornming.memo.activity.MainActivity

object MyDatabaseManager {
    private const val DATABASE_VISTION = 2
    private const val DATABASE_NAME = "noteDB"
    private val helper = MyDatabaseHelper(MainActivity.context, DATABASE_NAME, DATABASE_VISTION)
    @JvmStatic
    fun query(sql : String, selectionArgs : Array<String?>? = null) : Cursor {
        val db = helper.writableDatabase
        return db.rawQuery(sql, selectionArgs)
    }
    @JvmStatic
    fun insert(tableName : String,
               contentValues : ContentValues? = null) : Long {
        val db = helper.writableDatabase
        return db.insert(tableName, null, contentValues)
    }
    @JvmStatic
    fun update(tableName : String,
               whereClause : String? = null,
               strings : Array<String?>? = null,
               contentvalues : ContentValues = ContentValues()) : Int {
        val db = helper.writableDatabase
        return db.update(tableName, contentvalues, whereClause, strings)
    }
    @JvmStatic
    fun delete(tableName: String,
               whereClause : String? = null,
               args : Array<String?>? = null) : Int {
        val db = helper.writableDatabase
        return db.delete(tableName, whereClause, args)
    }
    @JvmStatic
    fun execSQL(sql : String) {
        val db = helper.writableDatabase
        db.execSQL(sql)
    }
}