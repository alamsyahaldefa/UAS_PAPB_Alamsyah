package com.example.uas_papb.data_class

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id : Int=0,

    @ColumnInfo("judul")
    val judul : String="",

    @ColumnInfo("gambar")
    val gambar : String="",

    @ColumnInfo("username")
    val username : String="",
)
