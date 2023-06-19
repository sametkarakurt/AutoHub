package com.example.autohub.view

import Post
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.autohub.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage

        val selectedPost = intent.getParcelableExtra<Post>("selectedPost")
        if (selectedPost != null) {
            // Seçilen gönderiyi kullanarak ayrıntı sayfasını güncelleyin
            if(selectedPost.username == auth.currentUser!!.displayName!!){
                binding.deleteButton.visibility = View.VISIBLE
            }

            binding.usernameText.text = selectedPost.username
            binding.brandText.text = selectedPost.brand
            binding.accessoriesText.text = selectedPost.accessories
            binding.commentText.text = selectedPost.comment

            if (selectedPost.downloadUrl != null) {
                Picasso.get().load(selectedPost.downloadUrl).into(binding.imageView1)
            }

            // İkinci resmi yükleme
            if (selectedPost.downloadUrl2 != null) {
                Picasso.get().load(selectedPost.downloadUrl2).into(binding.imageView2)
            }


            // Diğer verileri de burada kullanarak güncelleyebilirsiniz
        }
    }

    fun deletePost(view: View) {
        val selectedPost = intent.getParcelableExtra<Post>("selectedPost")
        db.collection("Posts")
            .whereEqualTo("username", auth.currentUser!!.displayName!!)
            .whereEqualTo("comment", selectedPost!!.comment)
            .whereEqualTo("brand", selectedPost!!.brand)
            .whereEqualTo("accessories", selectedPost!!.accessories)
            .whereEqualTo("downloadUrl", selectedPost.downloadUrl)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (documentSnapshot in querySnapshot) {
                        val postId = documentSnapshot.id
                        db.collection("Posts")
                            .document(postId)
                            .delete()
                            .addOnSuccessListener {
                                // Post deleted successfully
                                Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()

                                // Optionally, navigate back to the previous screen or perform any other actions
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // An error occurred while deleting the post
                                Toast.makeText(this, "Failed to delete post: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "No matching post found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // An error occurred while retrieving the post
                Toast.makeText(this, "Failed to retrieve post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}
