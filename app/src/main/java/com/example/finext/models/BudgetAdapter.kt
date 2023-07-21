package com.example.finext.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finext.R

class BudgetAdapter(private val budgetList: ArrayList<BudgetModel>) :
    RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.budget_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBudget = budgetList[position]
        holder.tvBudgetName.text = currentBudget.budgetValue
        holder.tvSelectedBudgetPeriod.text = currentBudget.selectedBudgetPeriod
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBudgetName: TextView = itemView.findViewById(R.id.tvBudgetName)
        val tvSelectedBudgetPeriod: TextView = itemView.findViewById(R.id.tvSelectedBudgetPeriod)


        init {
            itemView.setOnClickListener {
                mListener.onItemClick(adapterPosition)
            }
        }
    }
}
