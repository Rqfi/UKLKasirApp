package com.example.uklcashierapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.CartAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.Menu

class CartActivity : AppCompatActivity() {
    private var listCart = arrayListOf<Int?>()
    private var listMenu = arrayListOf<Menu>()

    lateinit var db: KasirDatabase

    lateinit var recycler: RecyclerView
    lateinit var checkoutButton: Button
    lateinit var cartAdapter: CartAdapter

    var id_user: Int = 0
    var addAgain: Boolean = false
    var id_transaksi: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.hide()

        db = KasirDatabase.getInstance(applicationContext)
        listCart = intent.getIntegerArrayListExtra("CART")!!
        id_user = intent.getIntExtra("id_user",0)
        id_transaksi = intent.getIntExtra("id_transaksi",0)
        addAgain = intent.getBooleanExtra("addAgain", false)

        recycler = findViewById(R.id.recyclerCart)
        checkoutButton = findViewById(R.id.checkOut)

        for (i in listCart){
            var menu = db.kasirDao().getMenu(i!!)
            listMenu.add(menu)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        listMenu.sortBy { it.jenis }
        cartAdapter = CartAdapter(listMenu)
        recycler.adapter = cartAdapter

        var total: Int = 0
        for (i in listMenu){
            total += i.harga
        }
        checkoutButton.text = "Checkout (" + listMenu.size + ") " + "Rp." + total

        checkoutButton.setOnClickListener {
            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
            intent.putExtra("id_user", id_user)
            intent.putExtra("id_transaksi", id_transaksi)
            intent.putExtra("addAgain", addAgain)
            var listIdMenu = arrayListOf<Int?>()
            for(i in listMenu){
                listIdMenu.add(i.id_menu)
            }
            intent.putIntegerArrayListExtra("list", listIdMenu)
            startActivity(intent)
        }
    }
}