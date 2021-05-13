package com.cmadushan.android.dr.ui.login


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


class LoginFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.setDrawer_Locked()
        auth= FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.signup)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)
        val link =view.findViewById<TextView>(R.id.register_link)

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            auth= FirebaseAuth.getInstance()
            if (usernameEditText.text.trim().toString().isNotEmpty() && passwordEditText.text.trim().toString().isNotEmpty()) {
                userSignIn(
                    usernameEditText.text.trim().toString(),
                    passwordEditText.text.trim().toString()
                )
            }
            else{
                loadingProgressBar.visibility = View.GONE
                Toast.makeText(context, "inputs aren't provided", Toast.LENGTH_LONG).show()
            }
        }
        link.setOnClickListener{
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }
    }
    private fun userSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
                    Log.d("task message", "successful...")
                    val action = LoginFragmentDirections.actionNavLoginToNavHome(auth.currentUser.uid)
                    findNavController().navigate(action)
                }
                else{
                    view?.findViewById<ProgressBar>(R.id.loading)?.visibility = View.GONE
                    val errorMessage: String = task.exception?.message.toString()
                    Toast.makeText(context, "Error : $errorMessage", Toast.LENGTH_LONG).show()
                }

            }
}



    override fun onDestroyView(){
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawer_Unlocked()
    }
      override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val user =auth.currentUser
            user?.let {
                val uid = user.uid
                val action = LoginFragmentDirections.actionNavLoginToNavHome(uid)
                findNavController().navigate(action)
            }

        }
    }

}