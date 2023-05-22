package com.example.uklcashierapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.Menu

class AddItemActivity : AppCompatActivity() {
    lateinit var nama: EditText
    lateinit var harga: EditText
    lateinit var jenis: Spinner
    lateinit var simpan: Button
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        initLocalDB()
        setDataSpinner()

        db = KasirDatabase.getInstance(applicationContext)

        simpan.setOnClickListener{
            if(nama.text.toString().isNotEmpty() && harga.text.toString().isNotEmpty() && jenis.selectedItem.toString() != "Pilih jenis item"){
                val namaProduk = nama.text.toString()
                val hargaProduk = harga.text.toString().toInt()
                val jenisProduk = jenis.selectedItem.toString()
                db.kasirDao().insertMenu(Menu(null, namaProduk, jenisProduk, hargaProduk))
                Toast.makeText(applicationContext, "Item berhasil ditambhakan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun initLocalDB(){
        nama = findViewById(R.id.namaProduk)
        harga = findViewById(R.id.hargaProduk)
        jenis = findViewById(R.id.pilihJenis)
        simpan = findViewById(R.id.simpan)
    }

    fun setDataSpinner(){
        val adapter = ArrayAdapter.createFromResource(applicationContext, R.array.jenis_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenis.adapter = adapter
    }
}