package com.dicoding.androidprogramming.submission3.activity

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.androidprogramming.submission3.R
import com.dicoding.androidprogramming.submission3.adapter.ListUserAdapter
import com.dicoding.androidprogramming.submission3.databinding.ActivityMainBinding
import com.dicoding.androidprogramming.submission3.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var list: ArrayList<User> = ArrayList()
    private lateinit var adapter: ListUserAdapter

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ListUserAdapter(list)
        binding.rvUsers.setHasFixedSize(true)

        getUser()
    }

    fun showRecyclerList(){
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
    }

    fun getUser(){
        binding.progressBar.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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 ")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users", object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String? = jsonObject.getString("login")
                        setUser(username)
                    }
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        )

    }
    fun getUserQuery(query: String){
        binding.progressBar.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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 ")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/search/users?q=$query", object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val item = jsonObject.getJSONArray("items")
                    for (i in 0 until item.length()){
                        val itemObject = item.getJSONObject(i)
                        val username: String = itemObject.getString("login")
                        setUser(username)
                    }
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        )

    }
    fun setUser(userID: String?){
        binding.progressBar.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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 ")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users/$userID", object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val user = User()
                    user.username = jsonObject.getString("login")
                    user.name = jsonObject.getString("name")
                    user.avatar = jsonObject.getString("avatar_url").toString()
                    user.company = jsonObject.getString("company").toString()
                    user.location = jsonObject.getString("location").toString()
                    user.repository = jsonObject.getString("public_repos")
                    user.followers = jsonObject.getString("followers")
                    user.following = jsonObject.getString("following")
                    list.add(user)
                    showRecyclerList()
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()){
                    return true
                }
                else{
                    list.clear()
                    getUserQuery(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()){
                    list.clear()
                    getUser()
                    return true
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        else if (item.itemId == R.id.action_favorite){
            val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}