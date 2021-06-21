package com.cmadushan.android.dr.ui.sessionlist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmadushan.android.dr.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class SessionListFragment : Fragment() {
    private val args by navArgs<SessionListFragmentArgs>()
    var db = FirebaseFirestore.getInstance()
    private var timeList= arrayListOf<String>()
    private var dateList= arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_session_list, container, false)
        getDoctor(args.doctorId)
        getTimeSlots()
        return root
    }

    private fun getDoctor(id: String) {
        db.collection("doctors").document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dname = document.getString("Name")
                    val specialization = document.getString("Specialization")
                    val hospital = document.getString("Hospital")
                    view?.findViewById<TextView>(R.id.dname)?.text = "Dr. $dname"
                        view?.findViewById<TextView>(R.id.specialization)?.text =
                            "( $specialization )"
                    view?.findViewById<TextView>(R.id.hospital)?.text = hospital


                }
            }
            .addOnFailureListener { exception ->
                d(TAG, "Error getting documents.", exception)
            }
    }

    private fun getTimeSlots() {
        db.collection("doctors").document(args.doctorId).collection("Times")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timestamp = document.getTimestamp("Time")
                    val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                    val getDate = Date(milliseconds)
                    val date = dateFormat.format(getDate).toString()
                    val timeFormat = SimpleDateFormat("HH:mm")
                    val getTime = Date(milliseconds)
                    val time = timeFormat.format(getTime).toString()
                    dateList.add(date)
                    timeList.add(time)

                }
                val adapter =CustomAdapter(dateList,timeList)
                val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView?.adapter =adapter
                recyclerView?.layoutManager = LinearLayoutManager(context)
            }
            .addOnFailureListener { exception ->
                d(TAG, "Error getting documents.", exception)
            }
    }
}