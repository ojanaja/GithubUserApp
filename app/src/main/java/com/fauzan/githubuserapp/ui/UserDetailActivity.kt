package com.fauzan.githubuserapp.ui

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.fauzan.githubuserapp.R
import com.fauzan.githubuserapp.adapter.SectionsPagerAdapter
import com.fauzan.githubuserapp.databinding.ActivityUserDetailBinding
import com.bumptech.glide.Glide
import com.fauzan.githubuserapp.database.DatabaseContract
import com.fauzan.githubuserapp.database.DatabaseHelper
import com.fauzan.githubuserapp.database.FavoriteHelper
import com.fauzan.githubuserapp.model.ResponseDetailUser
import com.fauzan.githubuserapp.viewModel.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.fauzan.githubuserapp.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME


class UserDetailActivity : AppCompatActivity() {

    private val detailUserViewModel: DetailUserViewModel by viewModels()
    private lateinit var binding: ActivityUserDetailBinding

    private var listFavorite = ArrayList<ResponseDetailUser>()
    private lateinit var helper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        username = intent.getStringExtra(EXTRA_USER).toString()
        showViewModel()
        detailUserViewModel.getIsLoading.observe(this, this::showLoading)

        helper = FavoriteHelper.getInstance(applicationContext)
        helper.open()
    }

    private fun showViewModel() {
        detailUserViewModel.detailUser(username)
        detailUserViewModel.getUserDetail.observe(this) { detailUser ->
            Glide.with(this)
                .load(detailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.imgAvatar)

            binding.tvName.text = detailUser.name
            binding.tvUsername.text = detailUser.login
            binding.tvCompany.text = detailUser.company
            binding.tvLocation.text = detailUser.location
            binding.tvRepositoryValue.text = detailUser.publicRepos
            binding.tvFollowersValue.text = detailUser.followers
            binding.tvFollowingValue.text = detailUser.following

            if (favoriteExist(username)) {
                binding.imageFavorite.isFavorite = true
                binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
                    if (favorite) {
                        listFavorite = helper.queryAll()
                        helper.insert(detailUser)
                        showToast("Added to Favorites")
                    } else {
                        listFavorite = helper.queryAll()
                        helper.delete(username)
                        showToast("Removed from Favorites")
                    }
                    helper.close()
                }
            } else {
                binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
                    if (favorite) {
                        listFavorite = helper.queryAll()
                        helper.insert(detailUser)
                        showToast("Added to Favorites")
                    } else {
                        listFavorite = helper.queryAll()
                        helper.delete(username)
                        showToast("Removed from Favorites")
                    }
                    helper.close()
                }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun favoriteExist(user: String): Boolean {
        val choose: String = DatabaseContract.FavoriteColumns.LOGIN + " =?"
        val chooseArg = arrayOf(user)
        val limit = "1"

        helper = FavoriteHelper(this)
        helper.open()

        val dataBaseHelper = DatabaseHelper(this@UserDetailActivity)
        val database: SQLiteDatabase = dataBaseHelper.writableDatabase
        val cursor: Cursor =
            database.query(TABLE_NAME, null, choose, chooseArg, null, null, null, limit)
        val exists: Boolean = cursor.count > 0
        cursor.close()

        database.close()
        return exists
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}