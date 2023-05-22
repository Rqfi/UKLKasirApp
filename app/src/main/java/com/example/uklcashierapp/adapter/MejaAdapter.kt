package com.example.uklcashierapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.entity.Meja

class MejaAdapter(var items: List<Meja>): RecyclerView.Adapter<MejaAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var namaMeja: TextView
        init{
            namaMeja = view.findViewById(R.id.namaMeja)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.meja_template, parent, false)
        return MejaAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaMeja.text = items[position].nomor_meja
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): Meja{
        return items.get(position)
    }
}