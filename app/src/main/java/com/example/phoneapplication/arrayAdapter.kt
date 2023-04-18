package com.example.phoneapplication

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide

class arrayAdapter(context: Context, resourceId: Int, items: List<cards>) :
    ArrayAdapter<cards>(context, resourceId, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var card_item = getItem(position)

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        }

        val name = view?.findViewById<TextView>(R.id.name)
        val answer = view?.findViewById<TextView>(R.id.answer)

        name?.text = card_item?.name
        answer?.text = card_item?.answer

        return view!!
    }
}
