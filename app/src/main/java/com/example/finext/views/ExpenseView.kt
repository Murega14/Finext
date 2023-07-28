package com.example.finext.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finext.R
import com.example.finext.fragments.ExpenseFragment
import com.example.finext.models.ExpAdapter
import com.example.finext.models.ExpenseModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExpenseView : AppCompatActivity() {

    private lateinit var expRecyclerView: RecyclerView
    private lateinit var tvLoadingData: EditText
    private lateinit var expList: ArrayList<ExpenseModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)

        expRecyclerView = findViewById(R.id.rvExp)
        expRecyclerView.layoutManager = LinearLayoutManager(this)
        expRecyclerView.setHasFixedSize(true)

        expList = arrayListOf()

        getExpenseData()
    }

    private fun getExpenseData() {
        expRecyclerView.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expList.clear()
                if (snapshot.exists()) {
                    for (expSnap in snapshot.children) {
                        val expData = expSnap.getValue(ExpenseModel::class.java)
                        expData?.let { expList.add(it) }
                    }
                    val mAdapter = ExpAdapter(expList)
                    expRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ExpAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ExpenseView, ExpenseFragment::class.java)

                            //put extras
                            intent.putExtra("expenseId", expList[position].expenseId)
                            intent.putExtra("label", expList[position].label)
                            intent.putExtra("amount", expList[position].amount)
                            startActivity(intent)
                        }
                    })

                    expRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error message or handle it in any other way you prefer
                Log.e("ExpenseView", "Database Error: ${error.message}")
                // Show an error message to the user or take any necessary actions
                Toast.makeText(this@ExpenseView, "Failed to fetch expense data", Toast.LENGTH_SHORT).show()
                // Hide the loading indicator and handle any other UI changes required
                expRecyclerView.visibility = View.VISIBLE
            }
        })
    }
}
