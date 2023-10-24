package com.example.uklcashierapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        supportActionBar?.hide()
        hideactionbar()
        changestatusbarcolor(R.color.transparent)
        initLocalDB()
        var emptyUsename: Boolean = false
        var emptyPassword: Boolean = false

        btnLogin.setOnClickListener{
            if(edtEmail.text.toString().trim().length == 0){
                edtEmail.error = "Email tidak boleh kosong"
                emptyUsename = true
            }

            if(edtPassword.text.toString().trim().length == 0){
                edtPassword.error = "Password tidak boleh kosong"
                emptyPassword = true
            }

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

    private fun hideactionbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView

            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let {
                    it.hide(WindowInsets.Type.statusBars())
                    it.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    private fun changestatusbarcolor(colorResId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window

            window.statusBarColor = ContextCompat.getColor(this,colorResId)
        }
    }
}