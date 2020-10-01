package com.buzzo.consumicredito

import android.os.Build
import java.util.*

class User(
    var username: String,
    var password: String) {

    fun getEncryptInfo(): String {

        val stringComposed = "$username:$password".toByteArray()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(stringComposed)
        } else {
            android.util.Base64.encodeToString(stringComposed, android.util.Base64.DEFAULT);
        }
    }

}