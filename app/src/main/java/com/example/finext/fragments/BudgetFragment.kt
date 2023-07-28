package com.example.finext.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.finext.R
import com.example.finext.models.BudgetModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BudgetFragment : Fragment() {

    private lateinit var budgetValue: EditText
    private lateinit var budgetbtnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_budget_fragment, container, false)

        budgetValue = view.findViewById(R.id.budgetvalue)
        budgetbtnSaveData = view.findViewById(R.id.budgetbtnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Budget")

        // Get the reference to the Spinner view
        val budgetPeriodSpinner = view.findViewById<Spinner>(R.id.budgetPeriodSpinner)

        // Define the options for the budget period dropdown menu
        val budgetPeriodOptions = arrayOf("1 week", "2 weeks", "3 weeks", "1 month")

        // Create the ArrayAdapter with the options and set it as the data source for the Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, budgetPeriodOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        budgetPeriodSpinner.adapter = adapter

        budgetbtnSaveData.setOnClickListener {
            saveBudget()
        }

        return view
    }

    private fun saveBudget() {
        val budgetValueText = budgetValue.text.toString()

        if (budgetValueText.isEmpty()) {
            budgetValue.error = "Please enter amount"
            return
        }



        // Get the selected budget period from the Spinner
        val budgetPeriodSpinner = view?.findViewById<Spinner>(R.id.budgetPeriodSpinner)

        // Convert the selected budget period to a Double
        val selectedBudgetPeriod: Double = when (budgetPeriodSpinner?.selectedItem.toString()) {
            "1 week" -> 1.0
            "2 weeks" -> 2.0
            "3 weeks" -> 3.0
            "1 month" -> 4.0
            else -> 1.0 // Default value
        }

        val budgetId = dbRef.push().key!!

        val budget = BudgetModel(budgetId, budgetValueText, selectedBudgetPeriod)

        dbRef.child(budgetId).setValue(budget)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_LONG).show()

                budgetValue.text.clear()
            }
            .addOnFailureListener { err ->
                Toast.makeText(requireContext(), "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

}
