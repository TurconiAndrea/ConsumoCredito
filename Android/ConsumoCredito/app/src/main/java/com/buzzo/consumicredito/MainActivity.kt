package com.buzzo.consumicredito

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var savedInformation: SavedInformation

    private val GB_TOTAL = 1
    private val GB_REMAINING = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomMenuSettings = createBottomMenu()

        val stringGBRemaining = intent.getStringExtra("GB_HOME")!!
        val tvGbRemaining = findViewById<TextView>(R.id.tvGbRemaining)
        val progressBarGBRemaining = findViewById<CircularProgressBar>(R.id.progressBarGBRemaining)

        val arrayOfGBValues = getArrayOfGBValues(stringGBRemaining)
        val gbTotal = arrayOfGBValues[GB_TOTAL]
        val gbRemaining = arrayOfGBValues[GB_REMAINING]

        setUpProgressBar(gbTotal)
        setProgressOnBar(gbRemaining)
        tvGbRemaining.text  = getFormattedText("$gbRemaining GB\nrimanenti")

        // Get the renewal info
        savedInformation = SavedInformation(this)
        val userAuthKey = savedInformation.getUserAuthKey()
        if (userAuthKey != null) {
            setRenewalInformation(userAuthKey)
            setPhoneSMSAndMMSInformation(userAuthKey)
        }

        btnMenuSetting.setOnClickListener {
            bottomMenuSettings.show()
        }
    }

    private fun openDialogConfirmationDelete() {
        val bottomConfirmDeleteMenu = createBottomConfirmDeleteMenu()
        bottomConfirmDeleteMenu.show()
    }

    private fun logout() {
        savedInformation.removeUserAuthKey()
        openLoadingActivity()
    }

    private fun openLoadingActivity() {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
    }

    private fun openHelpActivity() {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    private fun createBottomMenu(): RoundedBottomSheetDialog {
        val bottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_dialog_settings, null)

        val btnLogout = sheetView.findViewById<LinearLayout>(R.id.btnLogout)
        val btnHelp = sheetView.findViewById<LinearLayout>(R.id.btnHelp)
        btnLogout.setOnClickListener {
            openDialogConfirmationDelete()
        }
        btnHelp.setOnClickListener {
            bottomSheetDialog.dismiss()
            openHelpActivity()
        }

        bottomSheetDialog.setContentView(sheetView)
        return bottomSheetDialog
    }

    private fun createBottomConfirmDeleteMenu(): RoundedBottomSheetDialog {
        val bottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_dialog_delete_confirm, null)

        val tvConfirmDelete = sheetView.findViewById<TextView>(R.id.tvConfirmDelete)
        val tvCancelDelete = sheetView.findViewById<TextView>(R.id.tvCancelDelete)
        tvConfirmDelete.setOnClickListener { logout() }
        tvCancelDelete.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.setContentView(sheetView)
        return bottomSheetDialog
    }

    private fun getArrayOfGBValues(stringToParse: String): Array<Double> {
        val jsonObject = JSONArray(stringToParse)
        val gbObject = jsonObject[0] as String

        val gbUsed = getGBStringFormatted(gbObject.split("/")[0])
        val gbTotal = getGBStringFormatted(gbObject.split("/")[1])
        val gbRemaining = String.format("%.2f", gbTotal - gbUsed).toDouble()

        return arrayOf(gbUsed, gbTotal, gbRemaining)
    }


    private fun setUpProgressBar(max: Double) {
        progressBarGBRemaining.progressMax = max.toFloat()
        progressBarGBRemaining.progress = max.toFloat()
    }

    private fun setProgressOnBar(progress: Double) {
        progressBarGBRemaining.setProgressWithAnimation(progress.toFloat(), 2000)
    }

    private fun getFormattedText(toFormat: String): SpannableString {
        val spannableContent = SpannableString(toFormat)
        spannableContent.setSpan(RelativeSizeSpan(.7f), toFormat.indexOf("rimanenti"),  toFormat.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannableContent
    }

    private fun getGBStringFormatted(stringToFormat: String): Double {
        var resultGB = 0.0

        if (stringToFormat.contains("GB")) {
            resultGB = stringToFormat.trim().replace("GB", "").replace(",", ".").toDouble()
        } else {
            resultGB = stringToFormat.trim().replace("mb", "").replace(",", ".").toDouble()
            resultGB /= 1000
        }
        return resultGB
    }

    private fun getMoneyAndDateOfRenewal(stringToParse: String): Array<String> {
        val jsonObject = JSONArray(stringToParse)
        val money = jsonObject[0] as String
        val date = jsonObject[1] as String

        return arrayOf(money, date)
    }

    private fun getFormattedDate(date: String): String {
        val day = date.substring(0, 2)
        var month = date.substring(3,5)
        month = Month.getMonthFromValue(month)
        return "$day $month"
    }

    private fun getPhoneCallInformation(stringToParse: String): String {
        val jsonObject = JSONArray(stringToParse)
        val phoneCallInformation =  jsonObject[0]
        var phoneInfo = phoneCallInformation.toString().split(",")[0]
        phoneInfo = phoneInfo.replace("[", "")
        phoneInfo = phoneInfo.replace("\"", "")
        return phoneInfo
    }

    private fun getSMSInformation(stringToParse: String): String {
        val jsonObject = JSONArray(stringToParse)
        val smsInformation =  jsonObject[1]
        var smsInfo = smsInformation.toString().split(",")[0]
        smsInfo = smsInfo.replace("[", "")
        smsInfo = smsInfo.replace("\"", "")
        smsInfo = smsInfo.split(" ")[0]
        return "Messaggi inviati: $smsInfo"
    }

    private fun getMMSInformation(stringToParse: String): String {
        val jsonObject = JSONArray(stringToParse)
        val smsInformation =  jsonObject[3]
        var smsInfo = smsInformation.toString().split(",")[0]
        smsInfo = smsInfo.replace("[", "")
        smsInfo = smsInfo.replace("\"", "")
        smsInfo = smsInfo.split(" ")[0]
        return "MMS inviati: $smsInfo"
    }

    private fun setRenewalInformation(encryptionKey: String) {
        val iliadRequest = IliadRequest(applicationContext, encryptionKey)
        val callback = object : ServerCallback {
            override fun onSuccess(response: String) {
                Log.v("ECCOLO", "Response: $response")
                val moneyAndDateOfRenewal = getMoneyAndDateOfRenewal(response)
                tvCredit.text = moneyAndDateOfRenewal[0]
                tvDateRenewal.text = getFormattedDate(moneyAndDateOfRenewal[1])
            }
        }

        val route = IliadRoute.ROUTE_HOME.route + IliadRoute.ROUTE_RENEWAL.route
        iliadRequest.composeApiMethod(route, callback)
    }

    private fun setPhoneSMSAndMMSInformation(encryptionKey: String) {
        val iliadRequest = IliadRequest(applicationContext, encryptionKey)
        val callback = object : ServerCallback {
            override fun onSuccess(response: String) {
                Log.v("ECCOLO", "Response: $response")
                tvMinuteCall.text = getPhoneCallInformation(response)
                tvMessageSent.text = getSMSInformation(response)
                tvMmsSent.text = getMMSInformation(response)
            }
        }

        val route = IliadRoute.ROUTE_HOME.route + IliadRoute.ROUTE_ALL_INFORMATION.route
        iliadRequest.composeApiMethod(route, callback)
    }

    override fun onBackPressed() {}
}
