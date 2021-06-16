package com.cmadushan.android.dr.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cmadushan.android.dr.MainActivity
import com.cmadushan.android.dr.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
  var db = FirebaseFirestore.getInstance()
    var doctorlist = arrayListOf<String>()

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_home, container, false)
    arguments?.let {
      val args =HomeFragmentArgs.fromBundle(it)
      asignUserDetails(args.id)
    }
    return root
  }
    private fun asignUserDetails(id: String){
        val docRef = db.collection("users").document(id)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        d(TAG, "DocumentSnapshot data: ${document.data}")
                        val name =document.getString("Name")
                        val email =document.getString("Email")
                        if (name != null && email != null ) {
                            getImageLink(id, name, email)
                        }
                        db.collection("doctors")
                                .get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        val docName=document.getString("Name")
                                        doctorlist.add(docName.toString())
                                    }
                                    val spinner =view?.findViewById<Spinner>(R.id.doctorsSpinner)
                                    spinner?.onItemSelectedListener = this
                                   val dataAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, doctorlist)
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                    spinner?.adapter = dataAdapter
                                    d(TAG, doctorlist.toString())
                                }
                                .addOnFailureListener { exception ->
                                    d(TAG, "Error getting documents.", exception)
                                }

                    } else {
                        d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    d(TAG, "get failed with ", exception)
                }
    }

    private fun getImageLink(id: String, name: CharSequence, email: CharSequence){

        val ref = FirebaseStorage.getInstance().getReference("/images/$id")
                    ref.downloadUrl.addOnSuccessListener {
                        (activity as MainActivity?)?.loadnavheader(name, email, it.toString())
                    }
        }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item: String =  parent?.getItemAtPosition(position).toString()

        Toast.makeText(parent?.context, "Selected: $item", Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}



