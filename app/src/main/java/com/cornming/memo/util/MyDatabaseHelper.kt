package com.cornming.memo.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatabaseHelper(val context: Context, name : String, version : Int):
    SQLiteOpenHelper(context, name, null, version) {

    private val createPost = "create table post (id INTEGER PRIMARY key AUTOINCREMENT, " +
            "title text, " +
            "content text, " +
            "modify Data, " +
            "group_id INTRGER)"

    private val createGroup = "create table groups (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name text)"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createPost)
        Log.i("CreateTable", "Create Post Success")
        db?.execSQL(createGroup)
        Log.i("CreateTable", "Create Group Success")
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
        db?.execSQL("drop table if exists post")
        db?.execSQL("drop table if exists groups")

        this.onCreate(db)
    }



}