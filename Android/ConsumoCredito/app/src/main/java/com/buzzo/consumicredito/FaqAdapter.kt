package com.buzzo.consumicredito

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.faq_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class FaqAdapter(private val items: ArrayList<FAQ>, val context: Context, private val itemClickListener: ItemClickListener):
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>(), Filterable {

    var faqFilterList = ArrayList<FAQ>()

    init {
        faqFilterList = items
    }

    class FaqViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvQuestion = view.tvItemQuestion
        private val tvTopic = view.tvItemTopic

        fun bind(faq: FAQ) {
            tvQuestion.text = faq.question
            tvTopic.text = faq.topic
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FaqViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.faq_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val data = faqFilterList[position]
        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onCellClickListener(data)
        }
    }

    override fun getItemCount(): Int {
        return faqFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    faqFilterList = items
                } else {
                    val resultList = ArrayList<FAQ>()
                    for (row in items) {
                        if (row.answer.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    faqFilterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = faqFilterList
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                faqFilterList = results?.values as ArrayList<FAQ>
                notifyDataSetChanged()
            }

        }
    }

}