package com.cmadushan.android.dr.ui.sessionlist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.cmadushan.android.dr.R
import com.google.firebase.firestore.FirebaseFirestore

class SessionListFragment : Fragment() {
    private val args by navArgs<SessionListFragmentArgs>()
    var db = FirebaseFirestore.getInstance()
    var timeList= arrayListOf<String>()
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
                    d(TAG, "DocumentSnapshot data: ${document.data}")
                    val dname = document.getString("Name")
                    val specialization = document.getString("Specialization")
                    val hospital = document.getString("Hospital")
                    view?.findViewById<TextView>(R.id.dname)?.text =dname
                        view?.findViewById<TextView>(R.id.specialization)?.text = specialization
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
                    val Time = document.getTimestamp("Time")
                    timeList.add(Time?.toDate().toString())

                }
                d(TAG,timeList.toString())
            }
            .addOnFailureListener { exception ->
                d(TAG, "Error getting documents.", exception)
            }
    }
}