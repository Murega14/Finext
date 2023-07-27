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
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        setContentView(R.layout.activity_bills)

        billRecyclerView = findViewById(R.id.rvBills)
        billRecyclerView.layoutManager = LinearLayoutManager(this)
        billRecyclerView.setHasFixedSize(true)

        billList = arrayListOf()

        getBillData()
    }

    private fun getBillData() {
        billRecyclerView.visibility = View.GONE

        database = FirebaseDatabase.getInstance().getReference("Bills")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                billList.clear()
                if (snapshot.exists()) {
                    for (billsnap in snapshot.children) {
                        val billData = billsnap.getValue(BillModel::class.java)
                        billData?.let { billList.add(it) }
                    }
                    val mAdapter = BillAdapter(billList)
                    billRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BillAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@BillsView, BillpaymentFragment::class.java)

                            intent.putExtra("billId", billList[position].billId)
                            intent.putExtra("billname", billList[position].billname)
                            intent.putExtra("billamount", billList[position].billamount)
                            intent.putExtra("duedate", billList[position].duedate)
                            startActivity(intent)
                        }
                    })

                    billRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BillsView", "Database Error: ${error.message}")
                Toast.makeText(this@BillsView, "Failed to fetch bill data", Toast.LENGTH_SHORT).show()
                billRecyclerView.visibility = View.VISIBLE
            }
        })
=======
        setContentView(R.layout.`activity_bill_payment_fragment.xml`)
>>>>>>> master
    }
}