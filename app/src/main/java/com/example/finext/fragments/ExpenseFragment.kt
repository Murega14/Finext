package com.example.finext.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExpenseFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var expensesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance()
        expensesRef = database.reference.child("expenses")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ExpenseScreen()
                }
            }
        }

    @Preview
    @Composable
    fun ExpenseScreen() {
        val amount = remember { mutableStateOf("") }
        val category = remember { mutableStateOf("") }
        val label = remember { mutableStateOf("") }

        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add Expense", style = MaterialTheme.typography.h5)
                Spacer(modifier = Modifier.height(16.dp))
                ExpenseInputField("Amount", amount)
                ExpenseInputField("Label", label)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val expense = Expense(amount.value.toDouble(), category.value, label.value)
                        val expenseKey = expensesRef.push().key

                        if (expenseKey != null) {
                            expensesRef.child(expenseKey).setValue(expense)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Expense added successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to add expense",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Expense")
                }
            }
        }
    }

    @Composable
    fun ExpenseInputField(label: String, state: MutableState<String>) {
        TextField(
            value = state.value,
            onValueChange = { state.value = it },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

data class Expense(val amount: Double, val category: String, val label: String)
