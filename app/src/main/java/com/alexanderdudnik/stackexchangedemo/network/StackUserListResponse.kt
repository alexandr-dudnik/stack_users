package com.alexanderdudnik.stackexchangedemo.network

import com.google.gson.annotations.SerializedName

/**
 * Stack user list response from API
 *
 * @property userList
 * @property hasMore
 * @constructor Create empty Stack user list response
 */
data class StackUserListResponse(
    @SerializedName("items") val userList: List<StackUserResponse>,
    @SerializedName("has_more") val hasMore: Boolean
)
