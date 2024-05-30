package com.fauzan.githubuserapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fauzan.githubuserapp.api.ApiConfig
import com.fauzan.githubuserapp.model.Items
import com.fauzan.githubuserapp.model.ResponseSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val searchList = MutableLiveData<ArrayList<Items>>()
    val getSearchList: LiveData<ArrayList<Items>> = searchList

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    fun searchUser(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().search(username)
            client.enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        searchList.value = ArrayList(responseBody.items)
                    }
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}