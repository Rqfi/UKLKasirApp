package com.example.uklcashierapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.MejaAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.database.SwipeGesture
import com.example.uklcashierapp.entity.Meja
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListMejaActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var fabAddMeja: FloatingActionButton
    lateinit var adapter: MejaAdapter
    private var listMeja = mutableListOf<Meja>()
    lateinit var db: KasirDatabase

    var nama: String = ""
    var role: String = ""
    var id_user: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_meja)
        supportActionBar?.hide()
        changestatusbarcolor(R.color.transparent)

        recycler = findViewById(R.id.recyclerMeja)
        fabAddMeja = findViewById(R.id.btnAddMeja)
        db = KasirDatabase.getInstance(applicationContext)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = MejaAdapter(listMeja)
        recycler.adapter = adapter
        swipeToGesture(recycler)
        fabAddMeja.setOnClickListener{
            val intent = Intent(this@ListMejaActivity, AddMejaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getMeja()
    }
    fun getMeja(){
        listMeja.clear()
        listMeja.addAll(db.kasirDao().getAllMeja())
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
                            var adapter: MejaAdapter = itemRv.adapter as MejaAdapter
                            db.kasirDao().deleteMeja(adapter.getItem(position))
                            adapter.notifyItemRemoved(position)
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        ItemTouchHelper.RIGHT -> {
                            val intent = Intent(this@ListMejaActivity, EditMejaActivity::class.java)
                            var adapter: MejaAdapter = itemRv.adapter as MejaAdapter
                            var meja = adapter.getItem(position)
                            intent.putExtra("ID", meja.id_meja)
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
    private fun changestatusbarcolor(colorResId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window

            window.statusBarColor = ContextCompat.getColor(this,colorResId)
        }
    }
}