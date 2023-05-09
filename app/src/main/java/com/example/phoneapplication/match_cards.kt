package com.example.phoneapplication
import androidx.annotation.Keep

@Keep
class match_cards(
    internal var name: String = "",
    internal var dateLocation: String = "",
    internal var dateThisWeek: String = ""
) {
    constructor() : this("", "", "")
    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getLocation(): String {
        return dateLocation
    }

    fun setLocation(name: String) {
        this.dateLocation = dateLocation
    }

    fun getDate(): String {
        return dateThisWeek
    }

    fun setDate(name: String) {
        this.dateThisWeek = dateThisWeek
    }
}