package com.cornming.memo.model

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.text.BoringLayout
import android.widget.EditText
import androidx.core.content.contentValuesOf
import com.cornming.memo.activity.MainActivity
import com.cornming.memo.util.MyDatabaseManager
import com.cornming.memo.util.showToast
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PostGroup(id : Int = 0, name : String = "") {
    companion object {
        const val TABLE_NAME = "groups"
        const val ID = "id"
        const val NAME = "name"
        @JvmField
        val COLUMNS = arrayOf(ID, NAME)
        @JvmStatic
        fun allPostGroupList() : ArrayList<PostGroup> {
            val list = ArrayList<PostGroup>()
            val cursor = MyDatabaseManager.query("select * from $TABLE_NAME")
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                list.add(PostGroup(id, name))
            }
            return list
        }
        @JvmStatic
        fun deletePostGroup(group_id : Int) : Int {
            try {
                val postDelSql = "delete from ${Post.TABLE_NAME} where ${Post.GROUP_ID} in (${group_id})"
                Post.execSql(postDelSql)
                return MyDatabaseManager.delete(TABLE_NAME, "${ID} = ?", arrayOf(group_id.toString()))
            } catch (e : Exception) {
                "${e.message}".showToast()
                e.printStackTrace()
            }
            return  -1
        }
        @JvmStatic
        fun update(group_id: Int, group_name: String) : Int {

            return MyDatabaseManager.update(TABLE_NAME, "${ID} = ?",
                arrayOf(group_id.toString()),
                contentValuesOf(Pair(NAME, group_name)))
        }
        @JvmStatic
        fun insert(name : String) : Boolean {
            val cv = ContentValues().apply {
                put(NAME, name)
            }
            return MyDatabaseManager.insert(TABLE_NAME, cv) > 0
        }
    }
    val group_id = id
    var name = name
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
         return when (other) {
            !is PostGroup -> {
                false
            }
            else -> {
                this.group_id == other.group_id
            }
        }
    }

    override fun hashCode(): Int {
        var result = group_id
        result = 31 * result + name.hashCode()
        return result
    }
}