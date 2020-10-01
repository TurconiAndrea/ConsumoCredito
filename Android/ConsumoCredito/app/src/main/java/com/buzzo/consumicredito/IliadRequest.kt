package com.buzzo.consumicredito

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class IliadRequest(
    val context: Context,
    val authKey: String
) {

    private fun getVolleyInstance(): RequestQueue {
        return Volley.newRequestQueue(context)
    }

    fun composeApiMethod(url: String, callback: ServerCallback) {

        val stringRequest = object: StringRequest(
            Method.GET, url,
            Response.Listener<String> { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                Log.v("AAAAAAAA", "ERRORE: $error")
            })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Basic $authKey"
                    return headers
                }
            }

        stringRequest.retryPolicy = DefaultRetryPolicy(
            7000,
            3,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        getVolleyInstance().add(stringRequest)
    }
}