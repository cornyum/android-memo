package com.cornming.memo.activity

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cornming.memo.R
import com.cornming.memo.model.Post
import com.cornming.memo.model.PostGroup
import com.cornming.memo.util.getHourMin
import com.cornming.memo.util.showToast
import com.cornming.memo.util.toString
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.util.*
import kotlin.collections.ArrayList

class AddRecordActivity : AppCompatActivity() {
    lateinit var groupList : ArrayList<PostGroup>
    lateinit var adapter : ArrayAdapter<PostGroup>
    private lateinit var  group : PostGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)
        toolbar.title = "添加笔记"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)

        }

        okBut.setOnClickListener {
            addPost()
        }

        colckBut.setOnClickListener {
            setColock()
        }
        initSpinnerView()
        groupSpinner.setSelection(0)
        groupSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                group = groupList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
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

    private fun addPost() {
        val title = toString(note_title.text)
        val content = toString(note_content.text)
        val date = Date(System.currentTimeMillis())
        val groupid = group.group_id
        if (title == "" || content == "") {
            AlertDialog.Builder(this).apply {
                setNegativeButton("关闭", null)
                setTitle("不能为空")
                setMessage("标题和内容不能为空")
            }.create().show()
            return
        }
        if (Post.insert(title, content, date, groupid)) {
            "插入成功".showToast()
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this.finish()
        }
        return true
    }
}