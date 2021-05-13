package com.cmadushan.android.dr.ui.register


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cmadushan.android.dr.MainActivity
import com.cmadushan.android.dr.R
import com.cmadushan.android.dr.ui.login.LoginFragmentDirections
import com.cmadushan.android.dr.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap


class RegisterFragment : Fragment()  {
    private lateinit var auth: FirebaseAuth
    var db = FirebaseFirestore.getInstance()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.setDrawer_Locked()
        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameEditText = view.findViewById<EditText>(R.id.name)
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val addressEditText=view.findViewById<EditText>(R.id.address)
        val tpEditText=view.findViewById<EditText>(R.id.tp)
        val registerButton = view.findViewById<Button>(R.id.signup)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)
        val uploadphotobutton =view.findViewById<ImageButton>(R.id.uploadimage)

        uploadphotobutton.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent, 0)
        }

        registerButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            auth = FirebaseAuth.getInstance()
            if (nameEditText.text.trim().toString().isNotEmpty() &&
                usernameEditText.text.trim().toString().isNotEmpty() &&
                passwordEditText.text.trim().toString().isNotEmpty() &&
                addressEditText.text.trim().toString().isNotEmpty() &&
                tpEditText.text.trim().toString().isNotEmpty()
            ) {
                createUser(
                        usernameEditText.text.trim().toString(),
                        passwordEditText.text.trim().toString()
                )
            } else {
                loadingProgressBar.visibility = View.GONE
                makeText(context, "inputs aren't provided", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun createUser(email: String, password: String) {
        val nameEditText = view?.findViewById<EditText>(R.id.name)
        val usernameEditText = view?.findViewById<EditText>(R.id.username)
        val passwordEditText = view?.findViewById<EditText>(R.id.password)
        val addressEditText=view?.findViewById<EditText>(R.id.address)
        val tpEditText=view?.findViewById<EditText>(R.id.tp)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    addUser(
                        auth.currentUser.uid,
                            nameEditText?.text?.trim().toString(),
                            usernameEditText?.text?.trim().toString(),
                            passwordEditText?.text?.trim().toString(),
                            tpEditText?.text?.trim().toString(),
                            addressEditText?.text?.trim().toString()
                    )


                    makeText(context, "Successful", Toast.LENGTH_LONG).show()
                    Log.d("task message", "successful...")
                    val action = RegisterFragmentDirections.actionNavRegisterToNavHome(auth.currentUser.uid)
                    findNavController().navigate(action)

                } else {
                    view?.findViewById<ProgressBar>(R.id.loading)?.visibility = View.GONE
                    val errorMessage: String = task.exception?.message.toString()
                    makeText(context, "Error : $errorMessage", Toast.LENGTH_LONG).show()
                    Log.d("task message", "unsuccessful..." + task.exception)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawer_Unlocked()
    }
    fun addUser(id: String,name: String, email: String, password: String, tp: String, address: String){

        val user: MutableMap<String, Any> = HashMap()
        user["Id"]=id
        user["Name"] =  name
        user["Email"] =  email
        user["Password"] = password
        user["Tp"] =  tp
        user["Address"] = address

        db.collection("users").document(id)
            .set(user)
            .addOnSuccessListener { documentReference -> Log.d("firestore", "DocumentSnapshot added with ID: " + id)
                uploadImage(id,email)
            }
            .addOnFailureListener { e -> Log.w("firestore", "Error adding document", e) }
    }
    var selectedPhotoUri : Uri? =null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode==Activity.RESULT_OK && data!=null){
            selectedPhotoUri= data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectedPhotoUri)
            val  bitmapDrawable =BitmapDrawable(bitmap)
            view?.findViewById<ImageButton>(R.id.uploadimage)?.setBackgroundDrawable(bitmapDrawable)

        }
    }
    private fun uploadImage(id:String, email: String) {
        if (selectedPhotoUri== null)return
        val filename =UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("register","image uploaded")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("registr","link..."+it)
                        saveToDatabase(id,email,it.toString())
                    }
                }

    }
    private fun saveToDatabase(id: String,email:String,profileImageUrl: String) {
        val uid= id
        val ref =FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=  user(uid,email,profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("register","add to database")
                }
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val user =auth.currentUser
            user?.let {
                val uid = user.uid
                val action = RegisterFragmentDirections.actionNavRegisterToNavHome(uid)
                findNavController().navigate(action)
            }

        }
    }
}


