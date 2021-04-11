package com.dicoding.androidprogramming.submission3.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.androidprogramming.submission3.R
import com.dicoding.androidprogramming.submission3.adapter.SectionsPagerAdapter
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.NAME
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.dicoding.androidprogramming.submission3.database.FavHelper
import com.dicoding.androidprogramming.submission3.databinding.ActivityUserDetailsBinding
import com.dicoding.androidprogramming.submission3.entity.Favorite
import com.dicoding.androidprogramming.submission3.entity.User

class UserDetails : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding

    companion object{
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
    }

    private var isFav = false
    private lateinit var helper: FavHelper
    private var favorites: Favorite? = null
    private lateinit var imgAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = FavHelper.getInstance(applicationContext)
        helper.open()

        favorites = intent.getParcelableExtra(EXTRA_NOTE)
        if (favorites != null){
            val favUser = intent.getParcelableExtra<Favorite>(EXTRA_NOTE) as Favorite
            binding.tvNameDetail.text = favUser.name
            binding.tvUsernameDetail.text = favUser.username
            Glide.with(this).load(favUser.avatar).into(binding.imgAvatarDetail)
            binding.tvCompanyDetail.text = favUser.company
            binding.tvLocationDetail.text = favUser.location
            binding.tvRepositoryDetail.text = favUser.repository
            imgAvatar = favUser.avatar.toString()
            isFav = true
            val checked: Int = R.drawable.ic_baseline_favorite_24
            binding.btnFavorite.setImageResource(checked)
        }
        else{
            val user = intent.getParcelableExtra<User>(EXTRA_DATA) as User
            binding.tvNameDetail.text = user.name
            binding.tvUsernameDetail.text = user.username
            Glide.with(this).load(user.avatar).into(binding.imgAvatarDetail)
            binding.tvCompanyDetail.text = user.company
            binding.tvLocationDetail.text = user.location
            binding.tvRepositoryDetail.text = user.repository
            imgAvatar = user.avatar.toString()
        }

        binding.btnFavorite.setOnClickListener {
            val checked: Int = R.drawable.ic_baseline_favorite_24
            val unchecked: Int = R.drawable.ic_baseline_favorite_border_24
            if (isFav==true){
                helper.deleteByID(favorites?.username.toString())
                binding.btnFavorite.setImageResource(unchecked)
                isFav = false
            }
            else{
                val username = binding.tvUsernameDetail.text.toString()
                val name = binding.tvNameDetail.text.toString()
                val avatar = imgAvatar
                val company = binding.tvCompanyDetail.text.toString()
                val location = binding.tvLocationDetail.text.toString()
                val repository = binding.tvRepositoryDetail.text.toString()
                val favorite = "1"

                val values = ContentValues()
                values.put(USERNAME, username)
                values.put(NAME, name)
                values.put(AVATAR, avatar)
                values.put(COMPANY, company)
                values.put(LOCATION, location)
                values.put(REPOSITORY, repository)
                values.put(FAVORITE, favorite)

                isFav = true
                contentResolver.insert(CONTENT_URI, values)
                binding.btnFavorite.setImageResource(checked)
            }
        }

        binding.btnShare.setOnClickListener {
            val message = binding.tvUsernameDetail.text
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share to: "))
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

}