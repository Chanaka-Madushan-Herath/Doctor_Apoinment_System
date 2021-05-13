package com.cmadushan.android.dr.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cmadushan.android.dr.MainActivity
import com.cmadushan.android.dr.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
  var db = FirebaseFirestore.getInstance()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_home, container, false)
      val image = view?.findViewById<ImageView>(R.id.profilepichome)
    arguments?.let {
      val args =HomeFragmentArgs.fromBundle(it)
      asignUserDetails(args.id)

    }


    return root
  }
    fun asignUserDetails(id: String){
        val docRef = db.collection("users").document(id)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        val name =document.getString("Name")
                        val email =document.getString("Email")
                        if (name != null && email != null ) {
                            getImageLink(id,name, email)
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
    }

    fun getImageLink(id: String, name: CharSequence, email: CharSequence){
        database = Firebase.database.reference
        database.child("users").child(id).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val profilePicLink=it.child("profileImageUrl")
            Log.d("firebase", "url"+profilePicLink.value)
            val url =profilePicLink.value
            loadProfilePic(url.toString())
            (activity as MainActivity?)?.loadnavheader(name, email, url.toString())
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
    fun loadProfilePic(url: String): Any {
        val image = view?.findViewById<ImageView>(R.id.profilepichome)
        return Picasso.with(context).load(url).into(image)
    }
  }



