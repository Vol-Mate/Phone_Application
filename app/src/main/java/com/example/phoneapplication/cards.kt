package com.example.phoneapplication

import androidx.appcompat.app.AppCompatActivity

class cards : AppCompatActivity() {
    private var userId: String? = null
    private var name: String? = null
    private var profileImageUrl: String? = null
    fun cards(userId: String?, name: String?, profileImageUrl: String?) {
        this.userId = userId
        this.name = name
        this.profileImageUrl = profileImageUrl
    }

    fun getUserId(): String? {
        return userId
    }

    fun setUserID(userID: String?) {
        userId = userId
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