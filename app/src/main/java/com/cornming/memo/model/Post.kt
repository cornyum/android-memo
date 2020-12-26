package com.cornming.memo.model

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import com.cornming.memo.util.MyDatabaseManager
import com.cornming.memo.util.showToast
import com.cornming.memo.util.toString
import com.google.android.material.tabs.TabLayout
import java.io.Serializable
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class Post(id : Int = 0,
           title : String = "",
           content : String = "",
           modify : Date = Date(),
           group_id : Int = 0) : Serializable {
    companion object {
        const val TABLE_NAME = "post"
        const val ID = "id"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val TIME = "modify"
        const val GROUP_ID = "group_id"
        @JvmField
        val COLUMNS = arrayOf(ID, TITLE, CONTENT, TIME, GROUP_ID)
        @JvmStatic
        fun allPostList() : ArrayList<Post> {
            val list = ArrayList<Post>()
            val cursor = MyDatabaseManager.query("select * from $TABLE_NAME order by $TITLE desc, $TITLE")
            while (cursor.moveToNext()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val content = cursor.getString(cursor.getColumnIndex(CONTENT))
                val date = sdf.parse(cursor.getString(cursor.getColumnIndex(TIME)))
                val group_id = cursor.getInt(cursor.getColumnIndex(GROUP_ID))
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                list.add(Post(id, title, content, date, group_id))
            }
            return list
        }
        @JvmStatic
        fun deletePost(postid : Int) : Int {
            try {
                return MyDatabaseManager.delete(TABLE_NAME, "${ID} = ?", arrayOf(toString(postid)))
            } catch (e : Exception) {
                "${e.message}".showToast()
                e.printStackTrace()
            }
            return -1
        }
        @JvmStatic
        fun insert(title : String = "", content : String = "", modify : Date = Date(), group_id : Int = 0) : Boolean {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val cv = ContentValues().apply {
                put(TITLE, title)
                put(CONTENT, content)
                put(GROUP_ID, group_id)
                put(TIME, sdf.format(modify))
            }
            return MyDatabaseManager.insert(TABLE_NAME, cv) > 0
        }
        @JvmStatic
        fun query(sql : String, selectionArgs : Array<String?>? = null)  : ArrayList<Post> {
            val list = ArrayList<Post>()
            val cursor = MyDatabaseManager.query(sql, selectionArgs)
            while (cursor.moveToNext()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val content = cursor.getString(cursor.getColumnIndex(CONTENT))
                val date = sdf.parse(cursor.getString(cursor.getColumnIndex(TIME)))
                val group_id = cursor.getInt(cursor.getColumnIndex(GROUP_ID))
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                list.add(Post(id, title, content, date, group_id))
            }
            return list
        }
        @JvmStatic
        fun update(post : Post) : Int {
            val id = post.post_id
            val cv = ContentValues().apply {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                put(TITLE, post.title)
                put(CONTENT, post.content)
                put(GROUP_ID, post.group_id)
                put(TIME, sdf.format(post.modify))
            }

            return MyDatabaseManager.update(TABLE_NAME, "id = ?", arrayOf(id.toString()), cv)
        }
        @JvmStatic
        fun execSql(sql : String) {
            MyDatabaseManager.execSQL(sql)
        }

    }

    val post_id = id
    var content = content
    var title = title
    var group_id = group_id
    var modify = modify
    override fun toString(): String {
        return "post_id : ${post_id}\n" +
                "content : ${content}\n" +
                "group_id : ${group_id}\n" +
                "modify : ${modify}\n"
    }
}