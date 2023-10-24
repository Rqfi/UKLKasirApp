package com.example.uklcashierapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uklcashierapp.R
import com.example.uklcashierapp.entity.Menu

class ItemAdapter(var items: List<Menu>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var onAddClick: ((Menu) -> Unit)? = null
    var onSubstractClick: ((Menu) -> Unit)? = null
    var userRole: String = ""
    lateinit var jumlahText: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = items[position]
        if (userRole == "Manajer"){
            holder.addButton.visibility = View.GONE
            holder.substractButton.visibility = View.GONE
        } else {
            holder.addButton.visibility = View.VISIBLE
            holder.substractButton.visibility = View.VISIBLE
        }
        holder.txtNamaMenu.text = items[position].nama_menu
        holder.txtHargaMenu.text = "Rp." + items[position].harga
        jumlahText = holder.jumlah
        holder.addButton.setOnClickListener{
            var i: Int = holder.jumlah.text.toString().toInt() + 1
            holder.jumlah.text = "" + i
            onAddClick?.invoke(menu)
        }
        holder.substractButton.setOnClickListener{
            var i: Int = holder.jumlah.text.toString().toInt()
            if(i > 0){
                i -= 1
                holder.jumlah.text = "" + i
                onSubstractClick?.invoke(menu)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    public fun getItem(position: Int): Menu {
        return items.get(position)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var txtNamaMenu: TextView
        var txtHargaMenu: TextView
        var jumlah: TextView
        var addButton: ImageView
        var substractButton: ImageView

        init {
            txtNamaMenu = view.findViewById(R.id.namaItem)
            txtHargaMenu = view.findViewById(R.id.hargaItem)
            jumlah = view.findViewById(R.id.txtJumlah)
            addButton = view.findViewById(R.id.buttonAdd)
            substractButton = view.findViewById(R.id.buttonSubstract)
        }
    }
}