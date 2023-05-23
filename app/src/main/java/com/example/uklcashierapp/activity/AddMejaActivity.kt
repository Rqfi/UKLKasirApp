package com.example.uklcashierapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.Meja

class AddMejaActivity : AppCompatActivity() {
    lateinit var inputNama: TextView
    lateinit var btnSimpanMeja: Button
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meja)
        supportActionBar?.hide()

        inputNama = findViewById(R.id.inputNamaMeja)
        btnSimpanMeja = findViewById(R.id.btnSimpanMeja)
        db = KasirDatabase.getInstance(applicationContext)

        btnSimpanMeja.setOnClickListener{
            if(inputNama.text.toString().isNotEmpty()){
                db.kasirDao().insertMeja(Meja(null, inputNama.text.toString()))
                Toast.makeText(applicationContext, "Meja berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}