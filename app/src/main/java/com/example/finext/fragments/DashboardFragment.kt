package com.example.finext.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finext.R
import com.example.finext.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private lateinit var budgetRef: DatabaseReference
    private lateinit var expenseRef: DatabaseReference

    private var totalBudget: Double = 0.0
    private var totalExpense: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        budgetRef = FirebaseDatabase.getInstance().getReference("Budget")
        budgetRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    totalBudget = snapshot.child("budgetamount").getValue(Double::class.java) ?: 0.0
                    updatePieChart()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error if needed
            }
        })

        expenseRef = FirebaseDatabase.getInstance().getReference("Expense")
        expenseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                totalExpense = snapshot.child("amount").getValue(Double::class.java)!!
                updatePieChart()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return binding.root
    }

    private fun updatePieChart() {
        val pieChart: PieChart = binding.pieChart

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(totalExpense.toFloat(), "Total Expense"))
        entries.add(PieEntry((totalBudget - totalExpense).toFloat(), "Remaining Budget"))

        val dataSet = PieDataSet(entries, "")
        setColors(dataSet, listOf(R.color.orange, R.color.olive), requireContext())
        dataSet.valueTextSize = 12f

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(R.color.black)
        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = false
        pieChart.invalidate()

        // Calculate the remaining budget and update the TextView
        val remainingBudget = totalBudget - totalExpense
        binding.tvRemainingBudget.text = "Remaining Budget: $remainingBudget"

    }

    private fun setColors(dataSet: PieDataSet, colors: List<Int>, context: Context) {
        dataSet.colors = colors.map { context.getColor(it) }
    }
}
