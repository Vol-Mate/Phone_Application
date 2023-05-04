package com.example.phoneapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class Array_match_adapter<T>(context: Context?, resourceId: Int, items: List<match_cards>?) :
    ArrayAdapter<match_cards>(context!!, resourceId, items!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val card_item = getItem(position)
        if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            convertView = inflater.inflate(R.layout.activity_match, parent, false)
        }
        val dateThisWeek = convertView!!.findViewById<View>(R.id.dateMatch) as TextView
        val dateLocation = convertView!!.findViewById<View>(R.id.dateLocation) as TextView
        dateThisWeek.text = card_item!!.getDate()
        dateLocation.text = card_item!!.getLocation()
        return convertView
    }

}