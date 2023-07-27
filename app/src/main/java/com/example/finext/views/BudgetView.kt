package com.example.finext.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finext.R
import com.example.finext.fragments.BudgetFragment
import com.example.finext.models.BudgetAdapter
import com.example.finext.models.BudgetModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BudgetView : AppCompatActivity() {

    private lateinit var budgetRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var budgetList: ArrayList<BudgetModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_view)

        budgetRecyclerView = findViewById(R.id.rvBudget)
        budgetRecyclerView.layoutManager = LinearLayoutManager(this)
        budgetRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        budgetList = arrayListOf()

        getBudgetData()
    }

    private fun getBudgetData() {
        budgetRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Budget")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                budgetList.clear()
                if (snapshot.exists()) {
                    for (budgetSnap in snapshot.children) {
                        val budgetData = budgetSnap.getValue(BudgetModel::class.java)
                        budgetData?.let { budgetList.add(it) }
                    }
                    val mAdapter = BudgetAdapter(budgetList)
                    budgetRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BudgetAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@BudgetView, BudgetFragment::class.java)

                            intent.putExtra("budgetId", budgetList[position].budgetId)
                            intent.putextra("budgetName", budgetList[position].budgetName)
                            intent.putExtra("budgetValue", budgetList[position].budgetValue)
                            intent.putExtra("budgetName", budgetList[position].budgetName)
                            intent.putExtra("budgetPeriod", budgetList[position].selectedBudgetPeriod)
                            startActivity(intent)
                        }
                    })

                    budgetRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BudgetView", "Database Error: ${error.message}")
                Toast.makeText(this@BudgetView, "Failed to fetch budget data", Toast.LENGTH_SHORT).show()
                budgetRecyclerView.visibility = View.VISIBLE
                tvLoadingData.visibility = View.GONE
            }
        })
    }
}
