package com.cmadushan.android.dr.ui.myBookings

import android.content.ContentValues.TAG
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cmadushan.android.dr.R
import com.cmadushan.android.dr.ui.sessionlist.CustomAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class BookingDetailsAdapter(private val referenceNum: List<String>): RecyclerView.Adapter<BookingDetailsAdapter.ViewHolder>() {

    var db = FirebaseFirestore.getInstance()
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val refNum: TextView = view.findViewById(R.id.bookingReference)
        val docName: TextView =view.findViewById(R.id.bookingDName)
        val date: TextView =view.findViewById(R.id.bookingDate)
        val time: TextView =view.findViewById(R.id.bookingTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingDetailsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val bookingView =inflater.inflate(R.layout.single_booking, parent, false)
         return ViewHolder(bookingView)
    }

    override fun onBindViewHolder(holder: BookingDetailsAdapter.ViewHolder, position: Int) {
        val reference = referenceNum[position]
            db.collection("Reservations").document(reference).get()
                .addOnSuccessListener { document ->
                 if (document != null)  {
                     val refNum=document.id
                     val name =document.getString("Name").toString()
                     val address=document.getString("Address").toString()
                     val email=document.getString("Email").toString()
                     val tp=document.getString("Tp").toString()
                     val docName=document.getString("Doctor").toString()
                     val specialization =document.getString("Specialization").toString()
                     val hospital=document.getString("Hospital").toString()
                     val timestamp = document.getTimestamp("Time")
                     val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
                     val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                     val getDate = Date(milliseconds)
                     val date = dateFormat.format(getDate).toString()
                     val timeFormat = SimpleDateFormat("HH:mm")
                     val getTime = Date(milliseconds)
                     val time = timeFormat.format(getTime).toString()
                     holder.refNum.text=refNum
                     holder.docName.text= "Dr.$docName"
                     holder.date.text=date
                     holder.time.text=time
                     holder.itemView.setOnClickListener {
                         if (timestamp != null) {
                        val bookingDetails= BookingDetails(name,address,email,tp,docName,specialization,hospital,timestamp)
                         val action = MyBookingsFragmentDirections.actionNavMyBookingsToViewDetailsFragment(bookingDetails)
                         holder.itemView.findNavController().navigate(action)
                     }
                     }

                 }
                }

    }

    override fun getItemCount()= referenceNum.size

}