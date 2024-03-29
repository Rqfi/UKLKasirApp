package com.example.uklcashierapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.User

class RegisterActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var selectGender: Spinner
    lateinit var edtAddress: EditText
    lateinit var edtPhone: EditText
    lateinit var edtEmail: EditText
    lateinit var edtUsername: EditText
    lateinit var edtPassword: EditText
    lateinit var selectRole: Spinner
    lateinit var btnSave: Button
    lateinit var btnBack: ImageView
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        changestatusbarcolor(R.color.transparent)

        initLocalDB()
        setDataSpinner()
        btnBack = findViewById(R.id.back)
        btnBack.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        var emptyName: Boolean = false
        var emptyAddress: Boolean = false
        var emptyPhone: Boolean = false
        var emptyEmail: Boolean = false
        var emptyUsername: Boolean = false
        var emptyPassword: Boolean = false
        btnSave.setOnClickListener{
            if(edtName.text.toString().trim().length == 0){
                edtName.error = "Nama tidak boleh kosong"
                emptyName = true
            }
            if(edtAddress.text.toString().trim().length == 0){
                edtAddress.error = "Alamat tidak boleh kosong"
                emptyAddress = true
            }
            if(edtPhone.text.toString().trim().length == 0){
                edtPhone.error = "Nomor Telepon tidak boleh kosong"
                emptyPhone = true
            }
            if(edtEmail.text.toString().trim().length == 0){
                edtEmail.error = "Email tidak boleh kosong"
                emptyEmail = true
            }
            if(edtUsername.text.toString().trim().length == 0){
                edtUsername.error = "Username tidak boleh kosong"
                emptyUsername = true
            }
            if(edtPassword.text.toString().trim().length == 0){
                edtPassword.error = "Password tidak boleh kosong"
                emptyPassword = true
            }
            if(edtName.text.toString().isNotEmpty() && edtAddress.text.toString().isNotEmpty() && edtPhone.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() && edtUsername.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()){
                db.kasirDao().insertUser(
                    User(
                        null,
                        edtName.text.toString(),
                        selectGender.selectedItem.toString(),
                        edtAddress.text.toString(),
                        edtPhone.text.toString(),
                        edtEmail.text.toString(),
                        edtUsername.text.toString(),
                        edtPassword.text.toString(),
                        selectRole.selectedItem.toString()
                    )
                )
                Toast.makeText(applicationContext, "Register Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    fun initLocalDB(){
        edtName = findViewById(R.id.edtName)
        selectGender = findViewById(R.id.selectGender)
        edtAddress = findViewById(R.id.edtAddress)
        edtPhone = findViewById(R.id.edtPhone)
        edtEmail = findViewById(R.id.edtEmail)
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        selectRole = findViewById(R.id.selectRole)
        btnSave = findViewById(R.id.btnSave)
        db = KasirDatabase.getInstance(applicationContext)
    }
    private fun setDataSpinner(){
        val genderAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.gender_array, android.R.layout.simple_spinner_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectRole.adapter = genderAdapter

        val roleAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.role_array, android.R.layout.simple_spinner_item)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectRole.adapter = roleAdapter
    }

    private fun changestatusbarcolor(colorResId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window

            window.statusBarColor = ContextCompat.getColor(this,colorResId)
        }
    }
}