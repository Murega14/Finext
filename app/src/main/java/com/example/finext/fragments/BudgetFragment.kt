package com.example.finext.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BudgetFragment : Fragment() {
    private lateinit var budgetRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        budgetRef = FirebaseDatabase.getInstance().reference.child("budgets")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BudgetDashboard()
            }
        }
    }

    @Preview
    @Composable
    fun BudgetDashboard() {
        val budgetAmount = remember { mutableStateOf("") }
        val periodOptions = listOf("1 day", "1 week", "2 weeks", "3 weeks", "1 month")
        val selectedPeriod = remember { mutableStateOf(periodOptions[0]) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Enter Budget",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = budgetAmount.value,
                onValueChange = { budgetAmount.value = it },
                label = { Text(text = "Budget Amount") },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            DropdownMenu(
                expanded = false,
                onDismissRequest = { /* Empty */ }
            ) {
                periodOptions.forEach { option ->
                    DropdownMenuItem(onClick = { selectedPeriod.value = option }) {
                        Text(text = option)
                    }
                }
            }
            Button(
                onClick = {
                    val budget = budgetAmount.value.trim()
                    val period = selectedPeriod.value
                    if (budget.isNotBlank()) {
                        val budgetData = mapOf(
                            "amount" to budget,
                            "period" to period
                        )
                        budgetRef.push().setValue(budgetData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Budget saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to save budget",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a budget amount",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(text = "Submit")
            }
        }
    }
}
