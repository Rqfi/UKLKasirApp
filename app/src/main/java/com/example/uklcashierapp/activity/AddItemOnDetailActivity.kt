package com.example.uklcashierapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.ItemAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.Menu

class AddItemOnDetailActivity : AppCompatActivity() {
    lateinit var recyclerMakanan: RecyclerView
    lateinit var recyclerMinuman: RecyclerView
    lateinit var adapterMakanan: ItemAdapter
    lateinit var adapterMinuman: ItemAdapter

    lateinit var db: KasirDatabase
    lateinit var checkoutButton: Button

    private var listMakanan = mutableListOf<Menu>()
    private var listMinuman = mutableListOf<Menu>()
    private var listCart = arrayListOf<Int?>()

    var id_transaksi: Int = 0
    var addAgain: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item_on_detail)
        supportActionBar?.hide()

        recyclerMakanan = findViewById(R.id.recyclerMakanan)
        recyclerMinuman = findViewById(R.id.recyclerMinuman)
        checkoutButton = findViewById(R.id.checkOut)

        db = KasirDatabase.getInstance(applicationContext)
        id_transaksi = intent.getIntExtra("id_transaksi", 0)

        adapterMakanan = ItemAdapter(listMakanan)
        adapterMakanan.onAddClick = {
            listCart.add(it.id_menu)
            checkoutButton.isEnabled = true
            checkoutButton.visibility = View.VISIBLE
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        adapterMinuman = ItemAdapter(listMinuman)
        adapterMinuman.onAddClick = {
            listCart.add(it.id_menu)
            checkoutButton.isEnabled = true
            checkoutButton.visibility = View.VISIBLE
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        adapterMakanan.onSubstractClick = {
            listCart.remove(it.id_menu)
            if(listCart.size == 0){
                checkoutButton.isEnabled = false
                checkoutButton.visibility = View.INVISIBLE
            }
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        adapterMinuman.onSubstractClick = {
            listCart.remove(it.id_menu)
            if(listCart.size == 0){
                checkoutButton.isEnabled = false
                checkoutButton.visibility = View.INVISIBLE
            }
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        recyclerMakanan.adapter = adapterMakanan
        recyclerMakanan.layoutManager = LinearLayoutManager(this)
        recyclerMinuman.adapter = adapterMinuman
        recyclerMinuman.layoutManager = LinearLayoutManager(this)
        checkoutButton.setOnClickListener{
            val intent = Intent(this@AddItemOnDetailActivity, CartActivity::class.java)
            addAgain = true
            intent.putIntegerArrayListExtra("CART", listCart)
            intent.putExtra("id_transaksi", id_transaksi)
            intent.putExtra("addAgain", addAgain)
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
}