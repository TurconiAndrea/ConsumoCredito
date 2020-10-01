package com.buzzo.consumicredito

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_help.*
import kotlin.collections.ArrayList

class HelpActivity : AppCompatActivity(), ItemClickListener {

    lateinit var faqList: ArrayList<FAQ>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        createListOfFaq()

        val firstTwoFAQList = FAQ.getFirstTwoFAQ()
        faq1.setOnClickListener { createBottomFaqMenu(firstTwoFAQList[0]).show() }
        faq2.setOnClickListener { createBottomFaqMenu(firstTwoFAQList[1]).show() }
        tvHideTwoFaq.setOnClickListener { hideFirstTwoFaq() }
        ivBackArrow.setOnClickListener { finish() }
    }

    @SuppressLint("InflateParams")
    private fun createBottomFaqMenu(faq: FAQ): RoundedBottomSheetDialog {
        val bottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_dialog_faq_viewer, null)

        val tvTitle = sheetView.findViewById<TextView>(R.id.tvTitleFaq)
        val tvAnswer = sheetView.findViewById<TextView>(R.id.tvAnswerFaq)
        val tvTopic = sheetView.findViewById<TextView>(R.id.tvTopicFaq)
        tvTitle.text = faq.question
        tvAnswer.text = faq.answer
        tvTopic.text = faq.topic

        bottomSheetDialog.setContentView(sheetView)
        return bottomSheetDialog
    }

    @SuppressLint("SetTextI18n")
    private fun hideFirstTwoFaq() { tvHideTwoFaq
        if (tvHideTwoFaq.text == "Nascondi") {
            tvHideTwoFaq.text = "Mostra"
            llTwoFaq.visibility = View.GONE
        } else {
            tvHideTwoFaq.text = "Nascondi"
            llTwoFaq.visibility = View.VISIBLE
        }
    }

    private fun setFaqToList(faqList: ArrayList<FAQ>) {
        val adapter = FaqAdapter(faqList, this, this)
        rvFaqList.layoutManager = LinearLayoutManager(this)
        rvFaqList.adapter = adapter
        attachSearchBox(adapter)
    }

    private fun attachSearchBox(adapter: FaqAdapter) {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
    }

    private fun createListOfFaq() {
        val iliadRequest = IliadRequest(applicationContext, "")
        val callback = object : ServerCallback {
            override fun onSuccess(response: String) {
                val gson = Gson()
                faqList = ArrayList(gson.fromJson(response, Array<FAQ>::class.java).asList())
                setFaqToList(ArrayList(faqList))
            }
        }

        val route = IliadRoute.ROUTE_HOME.route + IliadRoute.ROUTE_FAQ.route
        iliadRequest.composeApiMethod(route, callback)
    }

    override fun onCellClickListener(data: FAQ) {
        searchView.clearFocus()
        createBottomFaqMenu(data).show()
    }

}