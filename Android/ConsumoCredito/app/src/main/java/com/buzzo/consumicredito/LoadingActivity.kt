package com.buzzo.consumicredito

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val savedInformation = SavedInformation(applicationContext)
        val userAuthKey = savedInformation.getUserAuthKey()

        if (savedInformation.isFirstRun() || userAuthKey.isNullOrEmpty()) {
            openLoginActivity()
            savedInformation.setFirstRunDone()
        } else {
            getGBConsumptions(userAuthKey)
        }
    }

    private fun openLoginActivity() {
        Handler().postDelayed(Runnable {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }, 2000)
    }

    private fun openMainActivity(stringGB: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("GB_HOME", stringGB)
        }
        startActivity(intent)
    }

    private fun getGBConsumptions(encryptionKey: String) {

        val iliadRequest = IliadRequest(applicationContext, encryptionKey)
        val callback = object : ServerCallback {
            override fun onSuccess(response: String) {
                Log.v("ECCOLO", "Response: $response")
                openMainActivity(response)
            }
        }

        val route = IliadRoute.ROUTE_HOME.route + IliadRoute.ROUTE_GB.route
        iliadRequest.composeApiMethod(route, callback)
    }

}