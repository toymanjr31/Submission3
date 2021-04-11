package com.dicoding.androidprogramming.submission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.androidprogramming.submission3.databinding.ItemRowFollowBinding
import com.dicoding.androidprogramming.submission3.entity.User

class FollowerAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowFollowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: ItemRowFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar).apply(RequestOptions().override(80,80))
                    .into(imgAvatarFollow)
                tvNameFollow.text = user.name
                tvUsernameFollow.text = user.username
            }
        }
    }
}