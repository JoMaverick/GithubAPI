package com.dicoding.github.ui.fav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.github.data.local.FavUser
import com.dicoding.github.data.response.ItemsItem
import com.dicoding.github.ui.detailuser.UserDetailActivity
import com.dicoding.github.ui.main.UserAdapter
import com.dicoding.github.ui.settings.SettingsActivity
import com.dicoding.githubapp.R
import com.dicoding.githubapp.databinding.ActivityFavBinding
import androidx.recyclerview.widget.DiffUtil

class FavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu -> {
                    val intent = Intent(this, FavActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        userAdapter = UserAdapter()

        viewModel = ViewModelProvider(this).get(FavViewModel::class.java)

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onClick(data: ItemsItem) {
                Intent(this@FavActivity, UserDetailActivity::class.java).also { intent ->
                    intent.putExtra(UserDetailActivity.USERNAME, data.login)
                    intent.putExtra(UserDetailActivity.ID, data.id)
                    intent.putExtra(UserDetailActivity.AVATAR, data.avatar_url)
                    startActivity(intent)
                }
            }
        })

        binding.apply {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = userAdapter
            rvFavorite.layoutManager = LinearLayoutManager(this@FavActivity)
        }
        viewModel.getFavUser()?.observe(this, { favUsers ->
            favUsers?.let {
                val list = mapList(it)
                updateList(list)
                showLoading(false)
            }
        })
        showLoading(true)

    }

    private fun mapList(users: List<FavUser>): List<ItemsItem> {
        return users.map { user ->
            ItemsItem(
                user.login,
                user.id,
                user.avatar_url
            )
        }
    }

    private fun updateList(newList: List<ItemsItem>) {
        val diffResult = DiffUtil.calculateDiff(UserAdapter.UserDiffCallback(userAdapter.getList(), newList))
        userAdapter.setList(newList)
        diffResult.dispatchUpdatesTo(userAdapter)
    }
    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}