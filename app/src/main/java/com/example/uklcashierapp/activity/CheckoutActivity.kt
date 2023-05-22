package com.example.uklcashierapp.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.DetailTransaksi
import com.example.uklcashierapp.entity.Menu
import com.example.uklcashierapp.entity.Transaksi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckoutActivity : AppCompatActivity() {
    lateinit var namaPelanggan: TextView
    lateinit var spinnerMeja: Spinner
    lateinit var dibayar: CheckBox
    lateinit var checkoutButton: Button
    lateinit var db: KasirDatabase

    var id_user: Int = 0
    var listIdMenu = arrayListOf<Int>()
    var listMenu = arrayListOf<Menu>()
    var addAgain: Boolean = false
    var id_transaksi: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        namaPelanggan = findViewById(R.id.namaPelanggan)
        spinnerMeja = findViewById(R.id.spinnerMeja)
        dibayar = findViewById(R.id.dibayar)
        checkoutButton = findViewById(R.id.checkOut)

        db = KasirDatabase.getInstance(applicationContext)
        id_user = intent.getIntExtra("id_user",0)
        id_transaksi = intent.getIntExtra("id_transaksi",0)
        listIdMenu = intent.getIntegerArrayListExtra("list")!!
        addAgain = intent.getBooleanExtra("addAgain",false)

        for (i in listIdMenu){
            var menu = db.kasirDao().getMenu(i)
            listMenu.add(menu)
        }

        setDataSpinner()
        var status = "Belum Bayar"

        if(addAgain == true){
            namaPelanggan.text = db.kasirDao().getTransaksi(id_transaksi).nama_pelanggan
            spinnerMeja.setSelection(db.kasirDao().getTransaksi(id_transaksi).id_meja - 1)
            if(db.kasirDao().getTransaksi(id_transaksi).status == "Dibayar"){
                dibayar.isChecked = true
            }
        }

        checkoutButton.setOnClickListener{
            if(addAgain == true){
                for (i in listMenu){
                    db.kasirDao().insertDetailTransaksi(
                        DetailTransaksi(
                        null,
                        id_transaksi,
                        i.id_menu!!,
                        i.harga
                    )
                    )
                }
                finish()
                finish()
                finish()
            } else {
                var formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
                var current = LocalDateTime.now().format(formatter)
                if(dibayar.isChecked){
                    status = "Dibayar"
                }
                var newTransaksi = Transaksi(null,
                    current,
                    id_user,
                    db.kasirDao().getIdMejaFromNama(spinnerMeja.selectedItem.toString()),
                    namaPelanggan.text.toString(),
                    status)
                db.kasirDao().insertTransaksi(newTransaksi)
                var idtransaksi = db.kasirDao().getIdTransaksiFromOther(
                    newTransaksi.tgl_transaksi,
                    newTransaksi.id_user,
                    newTransaksi.id_meja,
                    newTransaksi.nama_pelanggan,
                    newTransaksi.status)
                for (i in listMenu){
                    db.kasirDao().insertDetailTransaksi(DetailTransaksi(
                        null,
                        idtransaksi,
                        i.id_menu!!,
                        i.harga
                    ))
                }
                finish()
                finish()
            }
        }
    }
    private fun setDataSpinner(){
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, db.kasirDao().getAllNamaMeja())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeja.adapter = adapter
    }
}