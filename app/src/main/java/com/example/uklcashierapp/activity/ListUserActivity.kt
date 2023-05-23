package com.example.uklcashierapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.MejaAdapter
import com.example.uklcashierapp.adapter.UserAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.database.SwipeGesture
import com.example.uklcashierapp.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListUserActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var fabAddUser: FloatingActionButton
    lateinit var adapter: UserAdapter
    private var listUser = mutableListOf<User>()
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        supportActionBar?.hide()

        recycler = findViewById(R.id.recyclerUser)
        fabAddUser = findViewById(R.id.fabAddUser)
        db = KasirDatabase.getInstance(applicationContext)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(listUser)
        recycler.adapter = adapter
        swipeToGesture(recycler)
        fabAddUser.setOnClickListener{
            val intent = Intent(this@ListUserActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        getMeja()
    }
    fun getMeja(){
        listUser.clear()
        listUser.addAll(db.kasirDao().getAllUser())
        adapter.notifyDataSetChanged()
    }
    private fun swipeToGesture(itemRv: RecyclerView){
        val swipeGesture = object: SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int){
                val position = viewHolder.adapterPosition
                val actionBtnTapped = false

                try{
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            var adapter: UserAdapter = itemRv.adapter as UserAdapter
                            db.kasirDao().deleteUser(adapter.getItem(position))
                            adapter.notifyItemRemoved(position)
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        ItemTouchHelper.RIGHT -> {
                            val intent = Intent(this@ListUserActivity, EditUserActivity::class.java)
                            var adapter: UserAdapter = itemRv.adapter as UserAdapter
                            var user = adapter.getItem(position)
                            intent.putExtra("ID", user.id_user)
                            startActivity(intent)
                        }
                    }
                }
                catch (e: Exception){
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(itemRv)
    }
}