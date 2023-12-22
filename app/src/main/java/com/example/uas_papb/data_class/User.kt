package com.example.uas_papb.data_class

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(
    val username : String="",
    val email: String="",
    val password: String="",
    val role: String="",
)
