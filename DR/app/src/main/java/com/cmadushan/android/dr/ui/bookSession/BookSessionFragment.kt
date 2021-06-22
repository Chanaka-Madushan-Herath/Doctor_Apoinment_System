package com.cmadushan.android.dr.ui.bookSession

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cmadushan.android.dr.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class BookSessionFragment : Fragment() {
    val args: BookSessionFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var name: String? = null
    private var address: String? = null
    private var email: String? = null
    private var tp: String? = null
    private var dname: String? = null
    private var specialization: String? = null
    private var hospital: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_book_session, container, false)
        getUser()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDoctor(args.details.doctorId, args.details.timestamp)
        view.findViewById<RadioButton>(R.id.Yourself).setOnClickListener { getUser() }
        view.findViewById<RadioButton>(R.id.Other).setOnClickListener {setDetails()}
        view.findViewById<Button>(R.id.bookButton).setOnClickListener {
            bookSession()
            val action = BookSessionFragmentDirections.actionBookSessionFragmentToNavHome(auth.currentUser.uid)
            findNavController().navigate(action)
        }

    }

    private fun getDoctor(id: String, timeStamp: Timestamp) {
        db.collection("doctors").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                     dname = document.getString("Name")
                     specialization = document.getString("Specialization")
                     hospital = document.getString("Hospital")
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
                    view?.findViewById<TextView>(R.id.sessionDate)?.text = date
                    view?.findViewById<TextView>(R.id.sessionTime)?.text = time
                }
            }
            .addOnFailureListener { exception ->
                d(TAG, "Error getting documents.", exception)
            }
    }

    private fun getUser() {
        val docRef = db.collection("users").document(auth.currentUser.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.getString("Name").toString()
                    email = document.getString("Email").toString()
                    address = document.getString("Address").toString()
                    tp = document.getString("Tp").toString()
                    view?.findViewById<EditText>(R.id.fillName)?.setText(name)
                    view?.findViewById<EditText>(R.id.fillAdress)?.setText(address)
                    view?.findViewById<EditText>(R.id.fillEmail)?.setText(email)
                    view?.findViewById<EditText>(R.id.fillTP)?.setText(tp)
                }
            }
            .addOnFailureListener { exception ->
                d(TAG, "get failed with ", exception)
            }
    }
    private fun setDetails(){
        view?.findViewById<EditText>(R.id.fillName)?.setText("")
        view?.findViewById<EditText>(R.id.fillAdress)?.setText("")
        view?.findViewById<EditText>(R.id.fillEmail)?.setText("")
        view?.findViewById<EditText>(R.id.fillTP)?.setText("")
    }
    private fun bookSession(){
        val reservation: MutableMap<String, Any> = HashMap()
        reservation["Name"] =  name.toString()
        reservation["Email"] =  email.toString()
        reservation["Address"] = address.toString()
        reservation["Tp"] =  tp.toString()
        reservation["Doctor"] = dname.toString()
        reservation["Specialization"] = specialization.toString()
        reservation["Hospital"] =  hospital.toString()
        reservation["Time"] = args.details.timestamp

       val doc= db.collection("Reservations").document()
            doc.set(reservation)
            .addOnSuccessListener {
                val reference :MutableMap<String, Any> = HashMap()
                reference["Reservation_ID"] =doc.id

                db.collection("users").document(auth.currentUser.uid).collection("bookings").document()
                    .set(reference)
            }
            .addOnFailureListener { e -> Log.w("firestore", "Error adding document", e) }
    }


}