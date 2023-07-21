package com.example.finext.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.finext.R
import com.example.finext.models.BillModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BillpaymentFragment : Fragment() {

    private lateinit var billName: EditText
    private lateinit var billAmount: EditText
    private lateinit var dueDate: EditText
    private lateinit var billSaveButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_bill_payment_fragment, container, false)

        billName = view.findViewById(R.id.billname)
        billAmount = view.findViewById(R.id.billamount)
        dueDate = view.findViewById(R.id.duedate)
        billSaveButton = view.findViewById(R.id.billbtnsave)

        database = FirebaseDatabase.getInstance().getReference("Bills")

        billSaveButton.setOnClickListener {
            saveBill()
        }

        return view
    }

    private fun saveBill() {
        val billNameValue = billName.text.toString()
        val billAmountValue = billAmount.text.toString().toDoubleOrNull()
        val dueDateValue = dueDate.text.toString()

        if (billNameValue.isEmpty()) {
            billName.error = "Please enter bill name"
            return
        }

        if (billAmountValue == null) {
            billAmount.error = "Please enter a valid amount"
            return
        }

        if (dueDateValue.isEmpty()) {
            dueDate.error = "Please enter a valid date"
            return
        }

        val billId = database.push().key

        if (billId != null) {
            val bill = BillModel(billId, billNameValue, billAmountValue, dueDateValue)

            database.child(billId).setValue(bill)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show()
                    clearInputs()
                    setNotification(requireContext(), dueDateValue)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun clearInputs() {
        billName.text.clear()
        billAmount.text.clear()
        dueDate.text.clear()
    }

    private fun setNotification(context: Context, dueDate: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dueDateObj = LocalDate.parse(dueDate, formatter)
        val notificationDate = dueDateObj.minusDays(2)

        val channelId = "bill_notification_channel"
        val notificationId = 1

        val channel = NotificationChannel(
            channelId,
            "Bill Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        manager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Bill Reminder")
            .setContentText("Your bill is due in 2 days.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        } else {
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}
