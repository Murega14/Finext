package com.example.finext.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finext.R
import com.example.finext.models.BillModel

class BillAdapter(private val billList: ArrayList<BillModel>) :
    RecyclerView.Adapter<BillAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bill_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBill = billList[position]
        holder.tvBillName.text = currentBill.billname
        holder.tvBillAmount.text = currentBill.billamount.toString()
        holder.tvDueDate.text = currentBill.dueDate
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBillName: TextView = itemView.findViewById(R.id.tvBillName)
        val tvBillAmount: TextView = itemView.findViewById(R.id.tvBillAmount)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)

        init {
            itemView.setOnClickListener {
                mListener.onItemClick(adapterPosition)
            }
        }
    }
}
