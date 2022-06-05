package com.alexanderdudnik.stackexchangedemo.data

import com.alexanderdudnik.stackexchangedemo.network.StackUserResponse
import java.util.*

data class StackUserEntity(
    val accountId: Int,
    val profileImage: String,
    val displayName: String,
    val age: Int?,
    val reputation: Int,
    val badges: Map<String, Int>,
    val location: String,
    val creationDate: Date,
)

fun StackUserResponse.toStackUserEntity(): StackUserEntity {
    return StackUserEntity(
        accountId = this.accountId,
        profileImage = this.profileImage,
        displayName = this.displayName,
        age = this.age,
        reputation = this.reputation,
        badges = this.badges,
        location = this.location ?: "",
        creationDate = Date(this.creationDate * 1_000),
    )
}