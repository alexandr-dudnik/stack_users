package com.alexanderdudnik.stackexchangedemo.network

import com.google.gson.annotations.SerializedName

data class StackUserListResponse(
    @SerializedName("items") val userList: List<StackUserResponse>,
    @SerializedName("has_more") val hasMore: Boolean
)
