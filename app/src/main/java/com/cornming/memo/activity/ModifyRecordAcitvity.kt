package com.cornming.memo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.cornming.memo.R
import com.cornming.memo.model.Post
import com.cornming.memo.model.PostGroup
import com.cornming.memo.util.getHourMin
import com.cornming.memo.util.showToast
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.util.*
import kotlin.collections.ArrayList

class ModifyRecordAcitvity : AppCompatActivity() {
    private lateinit var groupList : ArrayList<PostGroup>
    private lateinit var adapter : ArrayAdapter<PostGroup>
    private lateinit var  group : PostGroup
    private lateinit var  nowPost : Post

    private fun initPost() {
        note_title.setText(nowPost.title)
        note_content.setText(nowPost.content)
        for (i in 0 until groupList.size) {
            if (groupList[i].group_id == nowPost.group_id) {
                groupSpinner.setSelection(i)
                break
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)
        toolbar.title = "修改笔记"
        setSupportActionBar(toolbar)
        nowPost = intent.getSerializableExtra("nowPost") as Post
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        okBut.setOnClickListener {
            modifyPost()
        }

        colckBut.setOnClickListener {
            setColock()
        }
        initSpinnerView()
        initPost()
        groupSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                group = groupList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }
    }
    private fun initSpinnerView() {
        groupList = PostGroup.allPostGroupList()
        adapter = ArrayAdapter<PostGroup>(this, R.layout.support_simple_spinner_dropdown_item, groupList)
        groupSpinner.adapter = adapter
    }

    private fun setColock() {
        val nowTime = System.currentTimeMillis()
        CardDatePickerDialog.builder(this).let {
            it.apply {
                setLabelText("年","月","日"," 时"," 分")
                displayTypes = intArrayOf(DateTimeConfig.HOUR, DateTimeConfig.MIN)
                model = CardDatePickerDialog.STACK
                backNow = false
                defaultMillisecond = nowTime
                minTime = nowTime
            }
            it.setOnChoose { millisecond->
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.apply {
                    val (hour, min) = getHourMin(millisecond)
                    putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "消息")
                    putExtra(AlarmClock.EXTRA_HOUR, hour)
                    putExtra(AlarmClock.EXTRA_MINUTES, min)
                    putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                    putExtra(AlarmClock.EXTRA_VIBRATE, true)
                }
                this.startActivity(intent)
            }
            it.setTitle("选择闹钟时间")
        }.build().show()
    }

    private fun modifyPost() {
        nowPost.title = note_title.text.toString()
        nowPost.content = note_content.text.toString()
        nowPost.modify = Date(System.currentTimeMillis())
        nowPost.group_id = group.group_id
        if (nowPost.title == "" || nowPost.content == "") {
            AlertDialog.Builder(this).apply {
                setNegativeButton("关闭", null)
                setTitle("不能为空")
                setMessage("标题和内容不能为空")
            }.create().show()
            return
        }
        if (Post.update(nowPost) > 0) {
            "更新成功".showToast()
            setResult(RESULT_OK)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this.finish()
        }
        return true
    }
}