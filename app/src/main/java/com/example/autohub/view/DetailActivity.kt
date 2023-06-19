package com.example.autohub.view

import Post
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.autohub.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val selectedPost = intent.getParcelableExtra<Post>("selectedPost")
        if (selectedPost != null) {
            // Seçilen gönderiyi kullanarak ayrıntı sayfasını güncelleyin
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
}
