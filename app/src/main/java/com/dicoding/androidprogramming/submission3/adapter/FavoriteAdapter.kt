package com.dicoding.androidprogramming.submission3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.androidprogramming.submission3.activity.UserDetails
import com.dicoding.androidprogramming.submission3.databinding.ItemRowUsersBinding
import com.dicoding.androidprogramming.submission3.entity.Favorite

class FavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FavoriteAdapter.ListViewHolder {
        val binding = ItemRowUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = listFavorite.size

    inner class ListViewHolder(private val binding: ItemRowUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite){
            with(binding){
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .apply(RequestOptions().override(120,120))
                    .into(imgAvatar)
                tvName.text = favorite.name
                tvUsername.text = favorite.username
                tvRepository.text = favorite.repository
                tvFollowers.text = favorite.followers
                tvFollowing.text = favorite.following
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition, object:CustomOnItemClickListener.OnItemClickCallback{
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, UserDetails::class.java)
                                intent.putExtra(UserDetails.EXTRA_POSITION, position)
                                intent.putExtra(UserDetails.EXTRA_NOTE, favorite)
                                activity.startActivity(intent)
                            }
                        } ))
            }
        }
    }
}