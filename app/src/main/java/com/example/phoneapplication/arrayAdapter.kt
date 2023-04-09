package com.example.phoneapplication

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
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
        val image = view?.findViewById<ImageView>(R.id.image)

        name?.text = card_item?.name

        when (card_item?.profileImageUrl) {
            "default" -> Glide.with(context).load(R.mipmap.ic_launcher).into(image!!)
            else -> {
                Glide.with(context).clear(image!!)
                Glide.with(context).load(card_item?.profileImageUrl).into(image!!)
            }
        }

        return view!!
    }
}
