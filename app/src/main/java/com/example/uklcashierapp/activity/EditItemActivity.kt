package com.example.uklcashierapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase

class EditItemActivity : AppCompatActivity() {
    lateinit var nama: EditText
    lateinit var harga: EditText
    lateinit var pilihTipe: Spinner
    lateinit var simpan: Button
    lateinit var db: KasirDatabase

    var id: Int = 0
    var nama_menu: String = ""
    var harga_menu: Int = 0
    var jenis: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
        supportActionBar?.hide()

        id = intent.getIntExtra("ID", 0)
        nama_menu = intent.getStringExtra("nama_menu")!!
        harga_menu = intent.getIntExtra("harga_menu", 0)
        jenis = intent.getStringExtra("jenis")!!

        init()
        setDataSpinner()
        nama
        db = KasirDatabase.getInstance(applicationContext)

        simpan.setOnClickListener{
            if(nama.text.toString().isNotEmpty() && harga.text.toString().isNotEmpty() && pilihTipe.selectedItem.toString() != "Pilih tipe item"){
                val namaProduk = nama.text.toString()
                val hargaProduk = harga.text.toString().toInt()
                val tipeProduk = pilihTipe.selectedItem.toString()
                db.kasirDao().updateMenu(namaProduk, tipeProduk, hargaProduk, id)
                Toast.makeText(applicationContext, "Item berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun init(){
        nama = findViewById(R.id.namaProduk)
        harga = findViewById(R.id.hargaProduk)
        pilihTipe = findViewById(R.id.pilihTipe)
        simpan = findViewById(R.id.simpan)
    }
    fun setDataSpinner(){
        val adapter = ArrayAdapter.createFromResource(applicationContext, R.array.jenis_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pilihTipe.adapter = adapter
    }
}