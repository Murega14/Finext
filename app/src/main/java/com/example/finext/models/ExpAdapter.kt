package com.example.finext.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finext.R

class ExpAdapter(private val expList: ArrayList<ExpenseModel>) :
    RecyclerView.Adapter<ExpAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exp_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentExp = expList[position]
        holder.tvExpName.text = currentExp.label
        holder.tvExpAmount.text = currentExp.amount.toString()
    }

    override fun getItemCount(): Int {
        return expList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExpName: TextView = itemView.findViewById(R.id.tvExpName)
        val tvExpAmount: TextView = itemView.findViewById(R.id.tvExpAmount)

        init {
            itemView.setOnClickListener {
                mListener.onItemClick(adapterPosition)
            }
        }
    }
}
