package com.dicoding.androidprogramming.submission3.activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.androidprogramming.submission3.R
import com.dicoding.androidprogramming.submission3.adapter.FavoriteAdapter
import com.dicoding.androidprogramming.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.androidprogramming.submission3.database.MappingHelper
import com.dicoding.androidprogramming.submission3.databinding.ActivityFavoriteBinding
import com.dicoding.androidprogramming.submission3.entity.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvFavorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavsAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if(savedInstanceState == null){
            loadFavsAsync()
        }
        else{
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list!=null){
                adapter.listFavorite = list
            }
        }
    }

    private fun loadFavsAsync() {
        GlobalScope.launch (Dispatchers.Main){
            binding.progressBar4.visibility = View.VISIBLE
            val deferredFavs = async (Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavs.await()
            binding.progressBar4.visibility = View.INVISIBLE
            if (favorites.size > 0){
                adapter.listFavorite = favorites
            }
            else{
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("No Data")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavsAsync()
    }
}