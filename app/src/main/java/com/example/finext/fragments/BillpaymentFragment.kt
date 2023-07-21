package com.example.finext.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finext.R
import com.example.finext.models.BillModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BillpaymentFragment : Fragment() {

    private lateinit var billname: EditText
    private lateinit var billamount: EditText
    private lateinit var duedate: EditText
    private lateinit var billbtnsave: Button

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_bill_payment_fragment, container, false)

        billname = view.findViewById(R.id.billname)
        billamount = view.findViewById(R.id.billamount)
        duedate = view.findViewById(R.id.duedate)
        billbtnsave = view.findViewById(R.id.billbtnsave)

        database = FirebaseDatabase.getInstance().getReference("Bills")

        billbtnsave.setOnClickListener {
            saveBillData()
        }

        return view
    }

    private fun saveBillData() {
        val billnameValue = billname.text.toString()
        val billamountValue = billamount.text.toString().toDoubleOrNull()
        val duedateValue = duedate.text.toString()

        if (billnameValue.isEmpty()) {
            billname.error = "Please enter a name"
            return
        }
        if (billamountValue == null) {
            billamount.error = "Please enter an amount"
            return
        }
        if (duedateValue.isEmpty()) {
            duedate.error = "Please enter a date"
            return
        }

        val billId = database.push().key
        billId?.let {
            val bills = BillModel(it, billnameValue, billamountValue, duedateValue)

            database.child(it).setValue(bills)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_LONG).show()
                        billname.text.clear()
                        billamount.text.clear()
                        duedate.text.clear()
                    } else {
                        Toast.makeText(requireContext(), "Data insertion failed", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
