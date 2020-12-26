package com.cornming.memo.activity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.cornming.memo.R
import com.cornming.memo.adapter.RecordAdapter
import com.cornming.memo.model.Post
import com.cornming.memo.model.PostGroup
import com.cornming.memo.util.*

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var context : Context
    }
    private lateinit var recordsList : ArrayList<Post>
    private lateinit var adapter: RecordAdapter
    private lateinit var groupList : ArrayList<PostGroup>
    private lateinit var groupMap : MutableMap<PostGroup, Int>
    private val ALLGROUP = -1

    private fun showRecords() {
        recordsList = Post.allPostList()
        changePostAdapter(recordsList)
    }

    private fun changePostAdapter(list : List<Post>) {
        adapter = RecordAdapter(this, R.layout.listitem_layout, list)
        recordListView.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }
        loadNavMenu()
        showRecords()
        recordListView.setOnCreateContextMenuListener { menu, v, menuInfo ->
            menuInflater.inflate(R.menu.contextmenu, menu)
        }
        itemAdd.setOnClickListener {
            val intent = Intent(this, AddRecordActivity::class.java)
            startActivityForResult(intent, ADDPOST)
        }
        navView.setNavigationItemSelectedListener {
            val item_group = it.groupId
            val itemid = it.itemId
            when (item_group) {
                0 -> {
                    showRecords()
                }
                1 -> {
                    val sql = "select * from ${Post.TABLE_NAME} where ${Post.GROUP_ID} = ?"
                    val sublist = Post.query(sql, arrayOf(toString(itemid)))
                    recordsList = sublist
                    changePostAdapter(sublist)
                }
                2 -> {
                    val intent = Intent(this, ManagerGroup::class.java)
                    startActivityForResult(intent, MANGTAG)
                    "编辑分组".showToast()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
        recordListView.setOnItemClickListener { parent, view, pos, id ->
            val nowPost = recordsList[pos]
            val intent = Intent(this, ModifyRecordAcitvity::class.java)
            intent.putExtra("nowPost", nowPost)
            startActivityForResult(intent, UPDATEITEM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ADDPOST -> {
                if (resultCode == RESULT_OK) {
                    showRecords()
                }
            }
            MANGTAG -> {
                loadNavMenu()
                if (resultCode == RESULT_OK) {
                    showRecords()
                }
            }
            UPDATEITEM -> {
                if (resultCode == RESULT_OK) {
                    showRecords()
                }
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as (AdapterView.AdapterContextMenuInfo)
        val pos = adapter.getItemId(info.position).toInt()
        when (item.itemId) {
            R.id.deletItems -> {
                delSelectPost(pos)
            }
            R.id.moveGroups -> {
                selectGroupAlert(pos)
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    private fun loadNavMenu() {
        groupList = PostGroup.allPostGroupList()
        val navMenu = navView.menu
        navMenu.clear()
        navMenu.add(0, ALLGROUP, 0, "全部分类").setIcon(R.drawable.tag)
        if (groupList.size != 0) {
            val submenu = navMenu.addSubMenu("详细分类")
            groupMap = mutableMapOf<PostGroup, Int>()
            for (item in groupList) {
                submenu.add(1, item.group_id, 0, item.name)
                groupMap.put(item, item.group_id)
            }
        }
        navMenu.add(2, 0, 0, "编辑分类").setIcon(R.drawable.edit)
    }

    private fun selectGroupAlert(pos : Int) {
        val now = recordsList[pos]
        AlertDialog.Builder(this).let {
            it.setTitle("选择分类")
                .setItems(groupList.map { it.name }.toTypedArray()) { _ , pos ->
                    val tagId  = groupMap[groupList[pos]]
                    tagId?.let {
                        now.group_id = it
                        Post.update(now)
                        adapter.notifyDataSetChanged()
                        "更新成功".showToast()
                    }
                }
                .setNegativeButton("取消", null)
        }.create().show()
    }

    private fun delSelectPost(pos : Int) {
        AlertDialog.Builder(this).apply {
            setMessage("是否删除此记录")
            setPositiveButton("确定"){ dialog, which ->
                val nowItem = recordsList[pos]
                recordsList.remove(nowItem)
                Post.deletePost(nowItem.post_id)
                adapter.notifyDataSetChanged()
                "删除成功".showToast()
            }
            setNegativeButton("取消", null)
        }.create().show()
    }
}