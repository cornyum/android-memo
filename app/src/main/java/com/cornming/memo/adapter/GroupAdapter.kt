package com.cornming.memo.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cornming.memo.R
import com.cornming.memo.model.PostGroup

class GroupAdapter(activity: Activity,
                   val resourceId : Int,
                   data : List<PostGroup>) : ArrayAdapter<PostGroup>(activity, resourceId, data) {
    inner class ViewHolder(val nameTv : TextView, val idTv : TextView)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder : ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val nameTv = view.findViewById<TextView>(R.id.groupName)
            val idTv = view.findViewById<TextView>(R.id.groupID)
            viewHolder = ViewHolder(nameTv, idTv)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val postgroup = getItem(position)
        postgroup?.let {
            viewHolder.nameTv.text = postgroup.name
            viewHolder.idTv.text = "ID : ${postgroup.group_id}"
        }

        return view
    }
}