package com.fauzan.githubuserapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fauzan.githubuserapp.api.ApiConfig
import com.fauzan.githubuserapp.model.ResponseFollow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val followers = MutableLiveData<ArrayList<ResponseFollow>>()
    val getFollowers: LiveData<ArrayList<ResponseFollow>> = followers

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    fun followers(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().followers(username)
            client.enqueue(object : Callback<ArrayList<ResponseFollow>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseFollow>>,
                    response: Response<ArrayList<ResponseFollow>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        followers.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponseFollow>>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}