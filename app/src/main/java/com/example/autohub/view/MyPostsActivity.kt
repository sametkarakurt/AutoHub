package com.example.autohub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import Post
import com.example.autohub.adapter.FeedRecyclerAdapter
import com.example.autohub.databinding.ActivityMyPostsBinding

class MyPostsActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMyPostsBinding
   private lateinit var auth: FirebaseAuth
   private lateinit var db : FirebaseFirestore
   private lateinit var postArrayList: ArrayList<Post>
   private lateinit var myPostsAdapter: FeedRecyclerAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMyPostsBinding.inflate(layoutInflater)
      setContentView(binding.root)

      auth = Firebase.auth
      db = Firebase.firestore
      postArrayList = ArrayList()
      getData()

      binding.recylerView2.layoutManager = LinearLayoutManager(this)
      myPostsAdapter = FeedRecyclerAdapter(postArrayList) { selectedPost ->
         goToDetailPage(selectedPost)



      }
      binding.recylerView2.adapter = myPostsAdapter
   }

   fun goToDetailPage(selectedPost: Post) {
      val intent = Intent(this, DetailActivity::class.java)
      intent.putExtra("selectedPost", selectedPost)
      startActivity(intent)
   }





   private fun getData() {
      db.collection("Posts")
         .whereEqualTo("username",auth.currentUser!!.displayName!!)
         .addSnapshotListener { value, error ->
            if (error != null) {
               Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {

               if (value != null && !value.isEmpty) {
                  val documents = value.documents
                  postArrayList.clear()

                  for (document in documents) {
                     val userName = document.get("username") as String
                     val userEmail = document.get("userEmail") as String
                     val brand = document.get("brand") as String
                     val comment = document.get("comment") as String
                     val accessories = document.get("accessories") as String
                     val downloadUrl = document.get("downloadUrl") as String
                     val downloadUrl2 = document.get("downloadUrl2") as String

                     val post = Post(
                        userName,
                        userEmail,
                        brand,
                        accessories,
                        comment,
                        downloadUrl,
                        downloadUrl2
                     )
                     postArrayList.add(post)
                  }
                  myPostsAdapter.notifyDataSetChanged()
               }
            }
         }
   }


}