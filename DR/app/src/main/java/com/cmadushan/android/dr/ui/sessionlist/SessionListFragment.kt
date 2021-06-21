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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class SessionListFragment : Fragment() {
    private val args by navArgs<SessionListFragmentArgs>()
    var db = FirebaseFirestore.getInstance()
    private var timestampList = arrayListOf<Timestamp>()

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
                timestampList.clear()
                for (document in result) {
                    val timestamp = document.getTimestamp("Time")
                    if (timestamp != null) {
                        timestampList.add(timestamp)
                    }
                }
                val adapter =CustomAdapter(timestampList,args.doctorId)
                val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView?.adapter =adapter
                recyclerView?.layoutManager = LinearLayoutManager(context)
            }
            .addOnFailureListener { exception ->
                d(TAG, "Error getting documents.", exception)
            }
    }
}