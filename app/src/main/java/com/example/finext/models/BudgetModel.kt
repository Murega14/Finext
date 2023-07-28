package com.example.finext.models

data class BudgetModel(
    var budgetId: String? = null,
    var budgetName: String? = null,
    var budgetValue: Double? = null,
    var selectedBudgetPeriod: Double? = null
)
