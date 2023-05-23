package com.example.uklcashierapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.User

class UserAdapter(var items: List<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var namaUser: TextView
        var gender: TextView
        var alamat: TextView
        var telepon: TextView
        var email: TextView
        var username: TextView
        var role: TextView

        init {
            namaUser = view.findViewById(R.id.namaUser)
            gender = view.findViewById(R.id.gender)
            alamat = view.findViewById(R.id.alamat)
            telepon = view.findViewById(R.id.telepon)
            email = view.findViewById(R.id.email)
            username = view.findViewById(R.id.username)
            role = view.findViewById(R.id.role)
        }
    }

    lateinit var db: KasirDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        db = KasirDatabase.getInstance(parent.context)
        var view = LayoutInflater.from(parent.context).inflate(R.layout.user_template, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaUser.text = items[position].nama
        holder.gender.text = items[position].gender
        holder.alamat.text = items[position].address
        holder.telepon.text = items[position].phone
        holder.email.text = items[position].email
        holder.username.text = items[position].username
        holder.role.text = items[position].role
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): User {
        return items.get(position)
    }
}