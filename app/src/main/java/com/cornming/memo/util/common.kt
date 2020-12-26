package com.cornming.memo.util

import android.content.Context
import android.widget.Toast
import com.cornming.memo.activity.MainActivity
import java.util.*

const val ADDPOST = 10
const val UPDATEITEM = 20
const val MANGTAG = 30
const val RENAME = 40
const val REMOVE = 50

fun String.showToast(time : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MainActivity.context, this, time).show()
}

fun Int.showToast(time : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MainActivity.context, this.toString(), time).show()
}

fun toString(any : Any?) : String {
    if (any == null) return "null"
    return any.toString()
}

fun getHourMin(time :Long = System.currentTimeMillis()) : Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}