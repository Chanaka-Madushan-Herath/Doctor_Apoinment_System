package com.cmadushan.android.dr.ui.sessionlist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.cmadushan.android.dr.R
import com.cmadushan.android.dr.ui.home.HomeFragmentArgs
import com.google.firebase.firestore.FirebaseFirestore

class SessionListFragment : Fragment() {
    private val args by navArgs<SessionListFragmentArgs>()
    var db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_session_list, container, false)
            getDoctor(args.doctorId)
        return root
    }

    private fun getDoctor(id:String){
        db.collection("doctors").document(id)
                .get()
                .addOnSuccessListener {document->
                    if (document != null) {
                        d(TAG, "DocumentSnapshot data: ${document.data}")
                        val name =document.getString("Name")
                        val Hospital =document.getString("Hospital")
                        view?.findViewById<TextView>(R.id.idshow)?.text = name
                        view?.findViewById<EditText>(R.id.editid)?.setText(Hospital)
                    }
                }
                .addOnFailureListener { exception ->
                    d(TAG, "Error getting documents.", exception)
                }
    }
   }