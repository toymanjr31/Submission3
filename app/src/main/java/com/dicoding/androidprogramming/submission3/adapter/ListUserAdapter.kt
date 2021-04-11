package com.dicoding.androidprogramming.submission3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.androidprogramming.submission3.activity.UserDetails
import com.dicoding.androidprogramming.submission3.databinding.ItemRowUsersBinding
import com.dicoding.androidprogramming.submission3.entity.User

lateinit var context: Context

class ListUserAdapter(private val listUser: ArrayList<User>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        context = viewGroup.context
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: ItemRowUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(120,120))
                    .into(imgAvatar)
                tvName.text = user.name
                tvUsername.text = user.username
                tvRepository.text = user.repository
                tvFollowers.text = user.followers
                tvFollowing.text = user.following

                itemView.setOnClickListener {
                    val userData = User()
                    userData.username = user.username
                    userData.name = user.name
                    userData.avatar = user.avatar
                    userData.company = user.company
                    userData.location = user.location
                    userData.repository = user.repository
                    userData.followers = user.followers
                    userData.following = user.following
                    userData.isFavorite = user.isFavorite

                    val intent = Intent(context, UserDetails::class.java)
                    intent.putExtra(UserDetails.EXTRA_DATA, userData)
                    intent.putExtra(UserDetails.EXTRA_FAVORITE, userData)
                    context.startActivity(intent)
                }

            }
        }
    }


}