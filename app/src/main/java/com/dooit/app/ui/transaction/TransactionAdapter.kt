package com.dooit.app.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dooit.app.R
import com.dooit.app.model.Transaction
import com.dooit.app.util.AppUtils
import kotlinx.android.synthetic.main.item_transaction.view.*


class TransactionAdapter(private var list: List<Transaction>,
                         private val listener: (Transaction) -> Unit
                  ) :
        RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    fun updateList(items: List<Transaction>) {
        list = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var itemView = inflater.inflate(R.layout.item_transaction, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val ctx = holder.itemView.context
        holder.itemView.tv_name.text = item.remark


        val colorRed = ctx.resources.getColor(R.color.md_red_400)
        val colorGreen = ctx.resources.getColor(R.color.md_green_600)

        if(item.debit>0) {
            holder.itemView.tv_amount.text = AppUtils.currency(item.debit)
            holder.itemView.tv_amount.setTextColor(colorGreen)
        } else {
            holder.itemView.tv_amount.text = "-"+AppUtils.currency(item.credit)
            holder.itemView.tv_amount.setTextColor(colorRed)
        }

        holder.itemView.setOnClickListener { listener(item) }


    }

    class ViewHolder(view: View)  : RecyclerView.ViewHolder(view) {}
}