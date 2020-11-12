package com.example.chatme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.chatme.R
import com.example.chatme.Utils.GridDataClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gridview_image.view.*

class GridAdapter(var list: ArrayList<GridDataClass>,var context: Context): BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var imgdata = list[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var myview = inflator.inflate(R.layout.gridview_image,null)
        Picasso.get().load(imgdata.img).into(myview.grid_image as ImageView)
        return myview
    }


}
