package com.example.uklcashierapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uklcashierapp.dao.KasirDao
import com.example.uklcashierapp.entity.*

@Database(
    entities = [DetailTransaksi::class, Meja::class, Menu::class, Transaksi:: class, User::class],
    version = 1
)
abstract class KasirDatabase: RoomDatabase() {
    abstract fun kasirDao(): KasirDao

    companion object{
        private var instance: KasirDatabase? = null
        fun getInstance(context: Context): KasirDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context, KasirDatabase::class.java, "kasir_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}