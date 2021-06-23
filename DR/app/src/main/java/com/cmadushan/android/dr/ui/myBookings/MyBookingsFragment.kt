package com.cmadushan.android.dr.ui.myBookings

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmadushan.android.dr.R
import com.cmadushan.android.dr.ui.sessionlist.CustomAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyBookingsFragment : Fragment() {

  var db = FirebaseFirestore.getInstance()
  var auth = FirebaseAuth.getInstance()
  private var bookingList = arrayListOf<String>()
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val root= inflater.inflate(R.layout.fragment_my_bookings, container, false)
    getBookingList()
    return root
  }


  private fun getBookingList() {
    db.collection("users").document(auth.currentUser.uid).collection("bookings").get()
      .addOnSuccessListener { result ->
        bookingList.clear()
        for (document in result) {
          val bookingRef = document.getString("Reservation_ID")
          if (bookingRef != null) {
            bookingList.add(bookingRef)
          }
        }
        val adapter = BookingDetailsAdapter(bookingList)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.bookingList)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
      }
  }
}


