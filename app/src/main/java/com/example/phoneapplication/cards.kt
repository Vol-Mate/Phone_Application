package com.example.phoneapplication

import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity

@Keep
class cards(
    internal var userId: String = "",
    internal var name: String = "",
    internal var age: Int = 0,
    internal var answer: String= ""
) {
    constructor() : this("", "", 0, "")

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

    fun getAge(): Int {
        return age
    }

    fun setAge(age: Int) {
        this.age = age
    }
}





