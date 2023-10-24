package com.example.uklcashierapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase

class EditTransaksiActivity : AppCompatActivity() {
    lateinit var inputNamaPelanggan: EditText
    lateinit var spinnerMeja: Spinner
    lateinit var simpanButton: Button
    lateinit var dibayar: CheckBox
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaksi)
        supportActionBar?.hide()

        inputNamaPelanggan = findViewById(R.id.namaPelanggan)
        spinnerMeja = findViewById(R.id.spinnerMeja)
        simpanButton = findViewById(R.id.simpan)
        dibayar = findViewById(R.id.dibayar)

        db = KasirDatabase.getInstance(applicationContext)

        setDataSpinner()

        // Get the ID of the transaction from the intent
        val id_transaksi: Int? = intent.getIntExtra("ID", 0)

        // Load existing data into UI elements
        loadExistingData(id_transaksi)

        simpanButton.setOnClickListener{
            var status = "Belum Dibayar"
            if(dibayar.isChecked){
                status = "Dibayar"
            }
            if (inputNamaPelanggan.text.toString().isNotEmpty()) {
                val id_transaksi: Int = intent.getIntExtra("ID", 0) ?: 0
                val idMeja = db.kasirDao().getIdMejaFromNama(spinnerMeja.selectedItem.toString())

                db.kasirDao().updateTransaksi(
                    inputNamaPelanggan.text.toString(),
                    idMeja,
                    status,
                    id_transaksi
                )
                finish()
            }

        }
    }

    private fun loadExistingData(id_transaksi: Int?) {
        if (id_transaksi != null) {
            val transaksi = db.kasirDao().getTransaksiById(id_transaksi)
            if (transaksi != null) {
                // Load existing data into UI elements
                inputNamaPelanggan.setText(transaksi.nama_pelanggan)

                val idMejaAsString = transaksi.id_meja.toString()

                val mejaIndex = db.kasirDao().getAllNamaMeja().indexOf(idMejaAsString)

                spinnerMeja.setSelection(mejaIndex)

                dibayar.isChecked = transaksi.status == "Dibayar"
            }
        }
    }


    private fun setDataSpinner(){
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, db.kasirDao().getAllNamaMeja())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeja.adapter = adapter
    }
}