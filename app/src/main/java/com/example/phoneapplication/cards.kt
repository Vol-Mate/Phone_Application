package com.example.phoneapplication

import androidx.appcompat.app.AppCompatActivity

class cards(internal var userId: String?, internal var name: String?, internal var profileImageUrl: String?) {
    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String?) {
        this.userId = userId
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getProfileImageUrl(): String? {
        return profileImageUrl
    }

    fun setProfileImageUrl(profileImageUrl: String?) {
        this.profileImageUrl = profileImageUrl
    }
}




