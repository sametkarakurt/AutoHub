package com.example.autohub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.autohub.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        val currentUser =  auth.currentUser

        if (currentUser != null){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun signUpVisible(view: View){
        binding.button2.visibility = View.VISIBLE
        binding.usernameText.visibility = View.VISIBLE
        binding.signUpTextButton.visibility = View.INVISIBLE
        binding.button.visibility = View.INVISIBLE
        binding.signInTextButton.visibility = View.VISIBLE
    }

    fun signInVisible(view: View){
        binding.button2.visibility = View.INVISIBLE
        binding.usernameText.visibility = View.INVISIBLE
        binding.signUpTextButton.visibility = View.VISIBLE
        binding.button.visibility = View.VISIBLE
        binding.signInTextButton.visibility = View.INVISIBLE

    }

    fun signInClicked(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG).show()
        }else {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener{ authResult ->

                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signUpClicked(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        val username = binding.usernameText.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty())
        {
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {authResult ->
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()

                authResult.user!!.updateProfile(profileUpdates)
                    .addOnSuccessListener {

                    }
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
            print("Kayıt başarıyla tamamlandı")
        }

    }




}