package com.example.uklcashierapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.adapter.DetailAdapter
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.DetailTransaksi

class ListDetailTransaksiActivity : AppCompatActivity() {
    lateinit var btnAdd: ImageButton
    lateinit var recycler: RecyclerView
    lateinit var total: TextView
    lateinit var adapter: DetailAdapter
    private var listDetail = arrayListOf<DetailTransaksi>()
    private var id_transaksi = 0
    private var totalHarga = 0
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_transaksi)
        supportActionBar?.hide()

        btnAdd = findViewById(R.id.btnAddTransaksi)
        recycler = findViewById(R.id.recyclerDetail)
        total = findViewById(R.id.total)

        id_transaksi = intent.getIntExtra("id_transaksi", 0)

        db = KasirDatabase.getInstance(applicationContext)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = DetailAdapter(listDetail)
        recycler.adapter = adapter

        if(db.kasirDao().getTransaksi(id_transaksi).status == "Dibayar"){
            btnAdd.isEnabled = false
            btnAdd.visibility = View.INVISIBLE
        }
        btnAdd.setOnClickListener{
            val intent = Intent(this@ListDetailTransaksiActivity, AddItemOnDetailActivity::class.java)
            intent.putExtra("id_transaksi", id_transaksi)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getDetail()
        totalHarga = 0
        for (i in listDetail){
            totalHarga += i.harga
        }
        total.text = "Rp." + totalHarga
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getDetail(){
        listDetail.clear()
        listDetail.addAll(db.kasirDao().getDetailTransaksi(id_transaksi))
        adapter.notifyDataSetChanged()
    }
}