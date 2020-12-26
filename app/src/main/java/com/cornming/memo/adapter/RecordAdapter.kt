package com.cornming.memo.adapter

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cornming.memo.R
import com.cornming.memo.model.Post

class RecordAdapter(activity: Activity,
                    val resourceId : Int,
                    data : List<Post>) : ArrayAdapter<Post>(activity, resourceId, data) {
    inner class ViewHolder(val titleTv : TextView, val timeTv :TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder : ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val titleTv = view.findViewById<TextView>(R.id.itemTitle)
            val timeTv = view.findViewById<TextView>(R.id.itemTime)
            viewHolder = ViewHolder(titleTv, timeTv)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val post = getItem(position)
        post?.let {
            viewHolder.titleTv.text = post.title
            viewHolder.timeTv.text = "时间：" + SimpleDateFormat("yyyy-MM-dd").format(post.modify)
        }

        return view
    }
}