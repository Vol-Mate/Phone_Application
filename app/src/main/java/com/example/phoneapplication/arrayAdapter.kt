package com.example.phoneapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class arrayAdapter(context: Context?, resourceId: Int, items: List<cards?>?) :
    ArrayAdapter<cards?>(context!!, resourceId, items!!) {
    //var context: Context? = null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val card_item = getItem(position)
        if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            convertView = inflater.inflate(R.layout.item, parent, false)
        }
        val name = convertView!!.findViewById<View>(R.id.name) as TextView
        val answer = convertView!!.findViewById<View>(R.id.answer) as TextView
        name.text = card_item!!.getName()
        answer.text = card_item!!.getAnswer()
        return convertView
    }
}