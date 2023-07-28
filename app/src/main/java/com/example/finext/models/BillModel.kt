package com.example.finext.models

data class BillModel(
    var billId: String? = null,
    var billname: String? = null,
    var billamount: Double? = null,
    var duedate: String? = null
)
