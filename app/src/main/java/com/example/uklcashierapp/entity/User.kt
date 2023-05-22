package com.example.uklcashierapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_user") var id_user: Int? = null,
    @ColumnInfo(name = "nama") var nama: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "alamat") var address: String,
    @ColumnInfo(name = "telepon") var phone: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "role") var role: String
)
