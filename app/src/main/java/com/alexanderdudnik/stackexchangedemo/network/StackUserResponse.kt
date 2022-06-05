package com.alexanderdudnik.stackexchangedemo.network

import com.google.gson.annotations.SerializedName
import java.util.*

data class StackUserResponse(
    @SerializedName("account_id") val accountId: Int,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("age") val age: Int?,
    @SerializedName("reputation") val reputation: Int,
    @SerializedName("badge_counts") val badges: Map<String, Int>,
    @SerializedName("location") val location: String?,
    @SerializedName("creation_date") val creationDate: Long,
)
