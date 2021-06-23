package com.cmadushan.android.dr.ui.profile

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cmadushan.android.dr.MainActivity
import com.cmadushan.android.dr.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

  private var auth = FirebaseAuth.getInstance()
  private var db = FirebaseFirestore.getInstance()
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
   getUser()
  }
  private fun getImage(){
    val ref = FirebaseStorage.getInstance().getReference("/images/${auth.currentUser.uid}")
    ref.downloadUrl.addOnSuccessListener {
      val image =view?.findViewById<ImageView>(R.id.imageView2)
      Picasso.with(context).load(it).into(image)
    }
  }
  private fun getUser(){
    val docRef = db.collection("users").document(auth.currentUser.uid)
    docRef.get()
      .addOnSuccessListener { document ->
        if (document != null) {
          getImage()
          val name =document.getString("Name")
          val email =document.getString("Email")
          val address =document.getString("Address")
          val tp =document.getString("Tp")

          val editName = view?.findViewById<EditText>(R.id.editName)
          val editAddress = view?.findViewById<EditText>(R.id.editAddress)
          val editEmail = view?.findViewById<EditText>(R.id.editEmail)
         val editTp = view?.findViewById<EditText>(R.id.editTp)

          editName?.setText(name)
          editAddress?.setText(address)
          editEmail?.setText(email)
          editTp?.setText(tp)

          view?.findViewById<Button>(R.id.buttonUpdate)?.setOnClickListener {
            update(editName?.text?.trim().toString(),editEmail?.text?.trim().toString(),editTp?.text?.trim().toString(),editAddress?.text?.trim().toString())
          }
      }
      }
      .addOnFailureListener { exception ->
        Log.d(ContentValues.TAG, "get failed with ", exception)
      }
  }
  private fun update(name: String, email: String, tp: String, address: String){
    val user: MutableMap<String, Any> = HashMap()
    user["Name"] =  name
    user["Email"] =  email
    user["Tp"] =  tp
    user["Address"] = address

    db.collection("users").document(auth.currentUser.uid)
      .set(user)
      .addOnSuccessListener {
        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_LONG).show()
      }
      .addOnFailureListener { e -> Log.w("firestore", "Error adding document", e) }
  }

}