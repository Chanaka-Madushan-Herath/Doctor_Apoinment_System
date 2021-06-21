package com.cmadushan.android.dr.ui.bookSession

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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class BookSessionFragment : Fragment() {
    val args: BookSessionFragmentArgs by navArgs()
    private val db= FirebaseFirestore.getInstance()
    private  var auth =FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_session, container, false)



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDoctor(args.details.doctorId,args.details.timestamp)
    }

    private fun getDoctor(id: String, timeStamp: Timestamp) {
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
                    val milliseconds = timeStamp.seconds * 1000 + timeStamp.nanoseconds / 1000000
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                    val getDate = Date(milliseconds)
                    val date = dateFormat.format(getDate).toString()
                    val timeFormat = SimpleDateFormat("HH:mm")
                    val getTime = Date(milliseconds)
                    val time = timeFormat.format(getTime).toString()
                    view?.findViewById<TextView>(R.id.sessionDate)?.text =date
                    view?.findViewById<TextView>(R.id.sessionTime)?.text =time
                   d(TAG, auth.currentUser.uid)
                }
            }
            .addOnFailureListener { exception ->
                d(TAG, "Error getting documents.", exception)
            }
    }
}