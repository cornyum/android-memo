package com.cornming.memo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.cornming.memo.R
import com.cornming.memo.adapter.GroupAdapter
import com.cornming.memo.model.PostGroup
import com.cornming.memo.util.REMOVE
import com.cornming.memo.util.RENAME
import com.cornming.memo.util.showToast
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_manager_group.*

class ManagerGroup : AppCompatActivity() {
    lateinit var groupList : ArrayList<PostGroup>
    lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_group)
        toolbar.title = "分类管理"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        showGroup()
        initListContentMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this.finish()
            R.id.addTag -> {
                addTages()
            }
        }
        return true
    }

    private fun addTages() {
        val edit = EditText(this)
        AlertDialog.Builder(this).let {
            it.setTitle("输入分类名称")
            it.setView(edit)
            it.setPositiveButton("确定") { dialog,  which ->
                val name = edit.text.toString()
                if (PostGroup.insert(name)) {
                    "创建成功".showToast()
                    showGroup()
                }
            }
            it.setNegativeButton("取消", null)
        }.create().show()
    }

    private fun showGroup() {
        groupList = PostGroup.allPostGroupList()
        changePostAdapter(groupList)
    }

    private fun changePostAdapter(list : List<PostGroup>) {
        adapter = GroupAdapter(this, R.layout.grouplist_layout, list)
        groupListView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.addtagmenu, menu)
        return true
    }

    private fun initListContentMenu() {
        groupListView.setOnCreateContextMenuListener { menu, v, menuInfo ->
            menu?.let {
                it.add(0, RENAME, 0, "重命名")
                it.add(0, REMOVE, 0, "删除分组")
            }
        }
    }

    private fun renameTag(position : Int) {
        val now = groupList[position]
        val edit = EditText(this)
        edit.setText(now.name)
        AlertDialog.Builder(this).let {
            it.setTitle("输入分类名称")
            it.setView(edit)
            it.setPositiveButton("确定") { dialog,  which ->
                groupList[position].name = edit.text.toString()
                PostGroup.update(now.group_id, groupList[position].name)
                adapter.notifyDataSetChanged()
            }
            it.setNegativeButton("取消", null)
        }.create().show()
    }

    private fun removeTag(position : Int) {
        AlertDialog.Builder(this).apply {
            setTitle("是否删除此分类")
            setMessage("删除后该分类内的所有文章都将被删除")
            setPositiveButton("确定") { dialog, which ->
                val nowItem = groupList[position]
                groupList.removeAt(position)
                PostGroup.deletePostGroup(nowItem.group_id)
                adapter.notifyDataSetChanged()
                "删除成功".showToast()
                setResult(RESULT_OK)
            }
            setNegativeButton("取消", null)
        }.create().show()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as (AdapterView.AdapterContextMenuInfo)
        val position = adapter.getItemId(info.position).toInt()

        when (item.itemId) {
            REMOVE -> {
                removeTag(position)
            }
            RENAME -> {
                renameTag(position)
            }
        }
        return super.onContextItemSelected(item)
    }
}