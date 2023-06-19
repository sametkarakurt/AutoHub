package com.example.autohub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.autohub.databinding.RecyclerRowBinding
import Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val postList: ArrayList<Post>, private val onItemClick: ((Post) -> Unit)? = null) :
    RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    inner class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedPost = postList[position]
                    onItemClick?.let { it1 -> it1(selectedPost) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = postList[position]
        with(holder.binding) {
            recyclerUsernameText.text = currentPost.username
            recyclerBrandText.text = currentPost.brand
            Picasso.get().load(currentPost.downloadUrl).into(recyclerImageView)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
