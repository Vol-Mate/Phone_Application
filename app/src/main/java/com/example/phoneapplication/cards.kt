package com.example.phoneapplication

import androidx.annotation.Keep

@Keep
class cards(
    internal var userId: String = "",
    internal var name: String = "",
    internal var age: String = "",
    internal var answer: String= ""
) {
    constructor() : this("", "", "", "")

    fun getUserId(): String {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getAnswer(): String {
        return answer
    }

    fun setAnswer(answer: String) {
        this.answer = answer
    }

    fun getAge(): String {
        return age
    }

    fun setAge(age: String) {
        this.age = age
    }
}





