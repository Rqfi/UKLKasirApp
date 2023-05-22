package com.example.uklcashierapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.database.KasirDatabase
import com.example.uklcashierapp.entity.DetailTransaksi

class DetailAdapter(var items: List<DetailTransaksi>): RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var namaMenu: TextView
        var hargaMenu: TextView

        init {
            namaMenu = view.findViewById(R.id.namaMenu)
            hargaMenu = view.findViewById(R.id.hargaMenu)
        }
    }

    lateinit var db: KasirDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        db = KasirDatabase.getInstance(parent.context)
        var view = LayoutInflater.from(parent.context).inflate(R.layout.detail_template, parent, false)
        return DetailAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaMenu.text = db.kasirDao().getMenu(items.get(position).id_menu).nama_menu
        holder.hargaMenu.text = "Rp." + db.kasirDao().getMenu(items.get(position).id_menu).harga.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}