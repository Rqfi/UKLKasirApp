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

        // Get item details from the intent
        id = intent.getIntExtra("ID", 0)

        // Load existing data into UI elements
        loadExistingData(id)



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

    private fun loadExistingData(id: Int) {
        // Fetch item details from the database based on the ID
        val menuItem = db.kasirDao().getMenuById(id)

        // Check if menuItem is not null before accessing its properties
        if (menuItem != null) {
            // Set existing data to UI elements
            nama.setText(menuItem.nama_menu ?: "")
            harga.setText(menuItem.harga?.toString() ?: "")

            // Set the selected item in the Spinner
            val jenisArray = resources.getStringArray(R.array.jenis_array)
            val position = jenisArray.indexOf(menuItem.jenis)
            if (position != -1) {
                pilihTipe.setSelection(position)
            }
        } else {
            // Handle the case when menuItem is null, for example, show an error message or handle it accordingly
            Toast.makeText(applicationContext, "Item not found", Toast.LENGTH_SHORT).show()
            // You might want to finish the activity or take other actions in case of an error
            finish()
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