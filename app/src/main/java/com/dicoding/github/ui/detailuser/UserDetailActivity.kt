package com.dicoding.github.ui.detailuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.github.ui.detailuserViewModel.UserDetailViewModel
import com.dicoding.github.ui.fav.FavActivity
import com.dicoding.github.ui.settings.SettingsActivity
import com.dicoding.githubapp.R
import com.dicoding.githubapp.databinding.ActivityUserDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val ID = "id"
        const val AVATAR = "avatar"
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: UserDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
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

        val username = intent.getStringExtra(USERNAME)!!
        val id = intent.getIntExtra(ID, 0)
        val avatar =  intent.getStringExtra(AVATAR)!!
        val bundle = Bundle()
        bundle.putString(USERNAME, username)

        viewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
        showLoading(true)
        viewModel.setDetailUser(username)
        viewModel.getDetailUser().observe(this, {
            if (it != null) {
                binding.apply {
                    userView.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .into(imageView)
                }
                showLoading(false)
            }
        })

        var _isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0){
                        binding.toggleFav.isChecked = true
                        _isCheck = true
                    } else{
                        binding.toggleFav.isChecked = false
                        _isCheck = false
                    }
                }
            }
        }

        binding.toggleFav.setOnClickListener{
            _isCheck = !_isCheck
            if(_isCheck){
                viewModel.addToFav(username, id, avatar)
            } else {
                viewModel.removeFromFav(id)
            }
            binding.toggleFav.isChecked = _isCheck
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply{
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }
    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}