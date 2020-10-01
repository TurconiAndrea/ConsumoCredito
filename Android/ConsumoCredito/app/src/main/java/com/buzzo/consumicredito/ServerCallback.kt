package com.buzzo.consumicredito

import org.json.JSONObject

interface ServerCallback {

    fun onSuccess(response: String)

}