package com.example.uklcashierapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.User

class LoginActivity : AppCompatActivity() {
    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var btnLogin: Button
    lateinit var btnRegister: TextView
    lateinit var db: KasirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initLocalDB()
        btnLogin.setOnClickListener{
            if(edtEmail.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()){
                var list: List<User> = db.kasirDao().login(edtEmail.text.toString(), edtPassword.text.toString())
                if(list.size > 0){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    val name = list[0].nama
                    val role = list[0].role
                    val id_user = list[0].id_user
                    intent.putExtra("name", name)
                    intent.putExtra("role", role)
                    intent.putExtra("id_user", id_user)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(applicationContext, "Pengguna tidak terdaftar", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    fun initLocalDB(){
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.login_btn)
        btnRegister = findViewById(R.id.register_link)
        db = KasirDatabase.getInstance(applicationContext)
    }
}