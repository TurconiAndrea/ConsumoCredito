package com.buzzo.consumicredito

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val ERROR = "LOGIN_FAILED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        cardView_background.setBackgroundResource(R.drawable.cardview_corner_editable)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            openErrorHandler("Username o password mancanti")
            return
        }

        val user = User(username, password)
        val authKey = user.getEncryptInfo()
        loaderLoginUi(true)
        loginToWebsite(authKey)
    }

    private fun loaderLoginUi(active: Boolean) {
        if (active) {
            tvLogin.visibility = View.GONE
            ldLogin.visibility = View.VISIBLE
            btnLogin.isClickable = false
        } else {
            tvLogin.visibility = View.VISIBLE
            ldLogin.visibility = View.GONE
            btnLogin.isClickable = true
        }

    }

    private fun openMainActivity(stringGB: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("GB_HOME", stringGB)
        }
        startActivity(intent)
    }

    private fun saveUserAuthKey(authKey: String) {
        val savedInformation = SavedInformation(applicationContext)
        savedInformation.setUserAuthKey(authKey)
    }

    private fun openErrorHandler(message: String) {
        etUsername.error = "Controlla la tua email"
        etPassword.error = "Controlla la tua password"
        etUsername.requestFocus()
        tvCredentialError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_animation))
        tvCredentialError.text = message
        tvCredentialError.setTextColor(Color.RED)
    }

    private fun loginToWebsite(encryptionKey: String) {

        val iliadRequest = IliadRequest(applicationContext, encryptionKey)
        val callback = object : ServerCallback {
            override fun onSuccess(response: String) {
                Log.v("LOGIN", "Response: $response")
                if (response == ERROR) {
                    loaderLoginUi(false)
                    openErrorHandler("Username o password errate")
                } else {
                    saveUserAuthKey(encryptionKey)
                    openMainActivity(response)
                }
            }
        }

        val route = IliadRoute.ROUTE_HOME.route + IliadRoute.ROUTE_GB.route
        iliadRequest.composeApiMethod(route, callback)
    }

}