package com.dicoding.github.data.response

import com.google.gson.annotations.SerializedName

data class ItemsItem(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatar_url")
	val avatar_url: String
)
