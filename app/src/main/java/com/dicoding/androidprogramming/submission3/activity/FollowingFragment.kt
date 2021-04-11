package com.dicoding.androidprogramming.submission3.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.androidprogramming.submission3.adapter.FollowingAdapter
import com.dicoding.androidprogramming.submission3.databinding.FragmentFollowingBinding
import com.dicoding.androidprogramming.submission3.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    var list: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowingAdapter
    private val binding get() = _binding!!

    companion object{
        private val TAG = FollowingFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingAdapter(list)
        binding.rvFollowing.setHasFixedSize(true)
        list.clear()
        val userData = activity!!.intent.getParcelableExtra<User>(UserDetails.EXTRA_DATA) as User
        getFollowing(userData.username.toString())
    }

    private fun showRecyclerList(){
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
    }

    fun getFollowing(userID: String){
        binding.progressBar3.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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 ")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users/$userID/following", object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar3.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String? = jsonObject.getString("login")
                        setFollowers(username)
                    }
                } catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar3.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        )

    }
    fun setFollowers(userID: String?){
        binding.progressBar3.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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 ")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users/$userID", object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar3.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val user = User()
                    user.username = jsonObject.getString("login")
                    user.name = jsonObject.getString("name")
                    user.avatar = jsonObject.getString("avatar_url").toString()
                    list.add(user)
                    showRecyclerList()
                } catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar3.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        )

    }
}