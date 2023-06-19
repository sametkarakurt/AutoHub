package com.example.autohub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.autohub.R
import com.example.autohub.adapter.FeedRecyclerAdapter
import com.example.autohub.databinding.ActivityFeedBinding
import Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var feedAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore
        postArrayList = ArrayList()
        getData()

        binding.recylerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(postArrayList) { selectedPost ->
            goToDetailPage(selectedPost)
        }
        binding.recylerView.adapter = feedAdapter
    }

    fun goToDetailPage(selectedPost: Post) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("selectedPost", selectedPost)
        startActivity(intent)
    }

    private fun getData() {
        db.collection("Posts")
            .orderBy("date", Query.Direction.DESCENDING)
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
                        feedAdapter.notifyDataSetChanged()
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.autohub_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_post -> {
                val intent = Intent(this, UploadActivity::class.java)
                startActivity(intent)
            }
            R.id.my_posts -> {
                val intent = Intent(this, MyPostsActivity::class.java)
                startActivity(intent)
            }
            else -> {
                auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
