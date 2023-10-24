package com.example.uklcashierapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.TransaksiAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.database.SwipeGesture
import com.example.uklcashierapp.entity.Transaksi
import android.util.Log
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat

class ListTransaksiActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var adapter: TransaksiAdapter
    lateinit var db: KasirDatabase
    private var listTransaksi = arrayListOf<Transaksi>()

    var nama: String = ""
    var role: String = ""
    var id_user: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_transaksi)
        supportActionBar?.hide()
        changestatusbarcolor(R.color.transparent)

        recycler = findViewById(R.id.recyclerTransaksi)
        db = KasirDatabase.getInstance(applicationContext)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = TransaksiAdapter(listTransaksi)
        recycler.adapter = adapter
        adapter.onHolderClick = {
            val intent = Intent(this@ListTransaksiActivity, ListDetailTransaksiActivity::class.java)
            intent.putExtra("id_transaksi", it.id_transaksi)
            startActivity(intent)
        }
        swipeToGesture(recycler)
    }

    override fun onResume() {
        super.onResume()
        Log.d("UserRole", "Role onResume: $role")
        getTransaksi()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getTransaksi(){
        listTransaksi.clear()
        listTransaksi.addAll(db.kasirDao().getAllTransaksi())
        adapter.notifyDataSetChanged()
    }

    private fun swipeToGesture(itemRv: RecyclerView) {
        if (role != "Manajer") {
            val swipeGesture = object : SwipeGesture(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val actionBtnTapped = false

                    try {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                var adapter: TransaksiAdapter = itemRv.adapter as TransaksiAdapter
                                db.kasirDao().deleteTransaksi(adapter.getItem(position))
                                adapter.notifyItemRemoved(position)
                                val intent = intent
                                finish()
                                startActivity(intent)
                            }
                            ItemTouchHelper.RIGHT -> {
                                val moveIntent = Intent(
                                    this@ListTransaksiActivity,
                                    EditTransaksiActivity::class.java
                                )
                                var adapter: TransaksiAdapter = itemRv.adapter as TransaksiAdapter
                                var transaksi = adapter.getItem(position)
                                moveIntent.putExtra("ID", transaksi.id_transaksi)
                                startActivity(moveIntent)
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val touchHelper = ItemTouchHelper(swipeGesture)
            touchHelper.attachToRecyclerView(itemRv)
        }
    }
    private fun changestatusbarcolor(colorResId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window

            window.statusBarColor = ContextCompat.getColor(this,colorResId)
        }
    }
}