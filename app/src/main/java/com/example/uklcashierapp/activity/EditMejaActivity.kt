package com.example.uklcashierapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase

class EditMejaActivity : AppCompatActivity() {
    lateinit var inputNama: EditText
    lateinit var btnSimpan: Button
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_meja)
        supportActionBar?.hide()

        inputNama = findViewById(R.id.inputNamaMeja)
        btnSimpan = findViewById(R.id.btnSimpanMeja)
        db = KasirDatabase.getInstance(applicationContext)

        var id = intent.getIntExtra("ID", 0)
        btnSimpan.setOnClickListener{
            if(inputNama.text.toString().isNotEmpty()){
                db.kasirDao().updateMeja(inputNama.text.toString(), id)
                finish()
            }
        }
    }
}