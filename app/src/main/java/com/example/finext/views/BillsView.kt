package com.example.finext.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finext.R
import com.example.finext.fragments.BillpaymentFragment
import com.example.finext.models.BillAdapter
import com.example.finext.models.BillModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BillsView : AppCompatActivity() {

    private lateinit var billRecyclerView: RecyclerView
    private lateinit var billList: ArrayList<BillModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bills_view)

        billRecyclerView = findViewById(R.id.rvBills)
        billRecyclerView.layoutManager = LinearLayoutManager(this)
        billRecyclerView.setHasFixedSize(true)

        billList = arrayListOf()

        getBillData()
    }

    private fun getBillData() {
        billRecyclerView.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Bills")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                billList.clear()
                if (snapshot.exists()) {
                    for (billSnap in snapshot.children) {
                        val billData = billSnap.getValue(BillModel::class.java)
                        billData?.let { billList.add(it) }
                    }
                    val mAdapter = BillAdapter(billList)
                    billRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BillAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@BillsView, BillpaymentFragment::class.java)

                            //put extras
                            intent.putExtra("billId", billList[position].billId)
                            intent.putExtra("billName", billList[position].billname)
                            intent.putExtra("billAmount", billList[position].billamount)
                            intent.putExtra("dueDate", billList[position].dueDate)
                            startActivity(intent)
                        }
                    })

                    billRecyclerView.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@BillsView, "No bill data available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error message or handle it in any other way you prefer
                Log.e("BillsView", "Database Error: ${error.message}")
                // Show an error message to the user or take any necessary actions
                Toast.makeText(this@BillsView, "Failed to fetch bill data", Toast.LENGTH_SHORT).show()
                // Hide the loading indicator and handle any other UI changes required
                billRecyclerView.visibility = View.VISIBLE
            }
        })
    }
}
