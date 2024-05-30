package com.fauzan.githubuserapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fauzan.githubuserapp.api.ApiConfig
import com.fauzan.githubuserapp.model.ResponseDetailUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val userDetail = MutableLiveData<ResponseDetailUser>()
    val getUserDetail: LiveData<ResponseDetailUser> = userDetail

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    fun detailUser(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().detailUser(username)
            client.enqueue(object : Callback<ResponseDetailUser> {
                override fun onResponse(
                    call: Call<ResponseDetailUser>,
                    response: Response<ResponseDetailUser>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        userDetail.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}