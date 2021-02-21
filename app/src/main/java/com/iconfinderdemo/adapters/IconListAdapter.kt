package com.iconfinderdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iconfinderdemo.R
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.util.Downloader
import com.iconfinderdemo.util.getProgressDrawable
import com.iconfinderdemo.util.loadImage
import kotlinx.android.synthetic.main.item_icon.view.*

class IconListAdapter(var icon: ArrayList<Icon>,var downloader: Downloader): RecyclerView.Adapter<IconListAdapter.IconViewHolder>() {

    fun updateIcons(newIcons:List<Icon>)
    {
        icon.clear()
        icon.addAll(newIcons)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon,parent,false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(icon[position])
        holder.itemView.setOnClickListener {
            downloader.donwload(icon[position].raster_sizes[6].formats[0].downloadUrl)
        }
    }

    override fun getItemCount(): Int {
       return icon.size
    }
    class IconViewHolder(view : View): RecyclerView.ViewHolder(view) {

        fun bind(icon: Icon)
        {
            val progressDrawable = getProgressDrawable(itemView.context)
            val previewUrl = icon.raster_sizes[6].formats[0].preview_url
            //Glide.with(itemView).load(previewUrl).into(itemView.iconIV)
            itemView.iconIV.loadImage(previewUrl,progressDrawable)
        }
    }
}