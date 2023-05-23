package com.example.uklcashierapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.User

class EditUserActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var selectGender: Spinner
    lateinit var edtAddress: EditText
    lateinit var edtPhone: EditText
    lateinit var edtEmail: EditText
    lateinit var edtUsername: EditText
    lateinit var edtPassword: EditText
    lateinit var btnSave: Button
    lateinit var selectRole: Spinner
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        supportActionBar?.hide()

        initLocalDB()
        setDataSpinner()
        var id = intent.getIntExtra("ID", 0)
        btnSave.setOnClickListener{
            if(edtName.text.toString().isNotEmpty() && edtAddress.text.toString().isNotEmpty() && edtPhone.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() && edtUsername.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()){
                db.kasirDao().updateUser(
                    edtName.text.toString(),
                    selectGender.selectedItem.toString(),
                    edtAddress.text.toString(),
                    edtPhone.text.toString(),
                    edtEmail.text.toString(),
                    edtUsername.text.toString(),
                    edtPassword.text.toString(),
                    selectRole.selectedItem.toString(),
                    id
                )
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
}