package com.example.uklcashierapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.ItemAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.database.SwipeGesture
import com.example.uklcashierapp.entity.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var recyclerMakanan: RecyclerView
    lateinit var recyclerMinuman: RecyclerView

    lateinit var adapterMakanan: ItemAdapter
    lateinit var adapterMinuman: ItemAdapter

    lateinit var db: KasirDatabase

    lateinit var btnMeja: ImageButton
    lateinit var btnTransaksi: ImageButton
    lateinit var btnCheckout: Button
    lateinit var fabAdd: FloatingActionButton
    lateinit var btnUser: ImageButton
    lateinit var btnLogoff: ImageButton

    private var listMakanan = mutableListOf<Menu>()
    private var listMinuman = mutableListOf<Menu>()
    private var listCart = arrayListOf<Int?>()

    var nama: String = ""
    var role: String = ""
    var id_user: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        changestatusbarcolor(R.color.transparent)

        recyclerMakanan = findViewById(R.id.recyclerMakanan)
        recyclerMinuman = findViewById(R.id.recyclerMinuman)

        btnMeja = findViewById(R.id.btnMeja)
        btnTransaksi = findViewById(R.id.btnTransaksi)
        btnCheckout = findViewById(R.id.checkOut)
        fabAdd = findViewById(R.id.fabAddItem)
        btnUser = findViewById(R.id.btnUser)
        btnLogoff = findViewById(R.id.btnOff)

        db = KasirDatabase.getInstance(applicationContext)

        adapterMakanan = ItemAdapter(listMakanan)
        adapterMakanan.userRole = "Admin"
        adapterMakanan.userRole = "Kasir"
        recyclerMakanan.adapter = adapterMakanan
        recyclerMakanan.layoutManager = LinearLayoutManager(this)
        adapterMakanan.onAddClick = {
            listCart.add(it.id_menu)
            btnCheckout.isEnabled = true
            btnCheckout.visibility = View.VISIBLE
            btnCheckout.text = "Checkout (" + listCart.size + ")"
        }
        adapterMinuman = ItemAdapter(listMinuman)
        adapterMinuman.userRole = "Admin"
        adapterMakanan.userRole = "Kasir"
        recyclerMinuman.adapter = adapterMinuman
        recyclerMinuman.layoutManager = LinearLayoutManager(this)
        adapterMinuman.onAddClick = {
            listCart.add(it.id_menu)
            btnCheckout.isEnabled = true
            btnCheckout.visibility = View.VISIBLE
            btnCheckout.text = "Checkout (" + listCart.size + ")"
        }
        adapterMakanan.onSubstractClick = {
            listCart.remove(it.id_menu)
            if(listCart.size == 0){
                btnCheckout.isEnabled = false
                btnCheckout.visibility = View.INVISIBLE
            }
            btnCheckout.text = "Checkout (" + listCart.size + ")"
        }
        adapterMinuman.onSubstractClick = {
            listCart.remove(it.id_menu)
            if(listCart.size == 0){
                btnCheckout.isEnabled = false
                btnCheckout.visibility = View.INVISIBLE
            }
            btnCheckout.text = "Checkout (" + listCart.size + ")"
        }
        recyclerMakanan.adapter = adapterMakanan
        recyclerMakanan.layoutManager = LinearLayoutManager(this)
        recyclerMinuman.adapter = adapterMinuman
        recyclerMinuman.layoutManager = LinearLayoutManager(this)

        nama = intent.getStringExtra("name")!!
        role = intent.getStringExtra("role")!!
        id_user = intent.getIntExtra("id_user", 0)
        Toast.makeText(applicationContext, "Logged in as " + nama, Toast.LENGTH_SHORT).show()

        if(role == "Kasir"){
            fabAdd.isEnabled = false
            fabAdd.visibility = View.INVISIBLE
            btnMeja.isEnabled = false
            btnMeja.visibility = View.INVISIBLE
            btnUser.isEnabled = false
            btnUser.visibility = View.INVISIBLE
        }
        if(role == "Admin") {
            swipeToGesture(recyclerMakanan)
            swipeToGesture(recyclerMinuman)
        }
        if(listCart.size == 0){
            btnCheckout.isEnabled = false
            btnCheckout.visibility = View.INVISIBLE
        }

        fabAdd.setOnClickListener{
            val intent = Intent(this@MainActivity, AddItemActivity::class.java)
            startActivity(intent)
        }
        btnMeja.setOnClickListener{
            val intent = Intent(this@MainActivity, ListMejaActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
        btnTransaksi.setOnClickListener{
            val intent = Intent(this@MainActivity, ListTransaksiActivity::class.java)
            startActivity(intent)
        }
        btnUser.setOnClickListener{
            val intent = Intent(this@MainActivity, ListUserActivity::class.java)
            startActivity(intent)
        }
        btnCheckout.setOnClickListener{
            val intent = Intent(this@MainActivity, CartActivity::class.java)
            intent.putIntegerArrayListExtra("CART", listCart)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
        btnLogoff.setOnClickListener{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getMenu()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getMenu(){
        listMakanan.clear()
        listMinuman.clear()
        listMakanan.addAll(db.kasirDao().getMenuFilterJenis("Makanan"))
        listMinuman.addAll(db.kasirDao().getMenuFilterJenis("Minuman"))
        adapterMakanan.notifyDataSetChanged()
        adapterMinuman.notifyDataSetChanged()
    }

    private fun swipeToGesture(itemRv: RecyclerView){
        if (role == "Admin") {
            val swipeGesture = object : SwipeGesture(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val actionBtnTapped = false

                    try {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                var adapter: ItemAdapter = itemRv.adapter as ItemAdapter
                                db.kasirDao().deleteMenu(adapter.getItem(position))
                                adapter.notifyItemRemoved(position)
                                val intent = intent
                                finish()
                                startActivity(intent)
                            }
                            ItemTouchHelper.RIGHT -> {
                                val moveIntent =
                                    Intent(this@MainActivity, EditItemActivity::class.java)
                                var adapter: ItemAdapter = itemRv.adapter as ItemAdapter
                                var menu = adapter.getItem(position)
                                moveIntent.putExtra("ID", menu.id_menu)
                                moveIntent.putExtra("nama_menu", menu.nama_menu)
                                moveIntent.putExtra("harga_menu", menu.harga)
                                moveIntent.putExtra("jenis", menu.jenis)
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