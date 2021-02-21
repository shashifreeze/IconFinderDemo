package com.iconfinderdemo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iconfinderdemo.R
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.util.Constants.KEY_ICON_SET_ID
import com.iconfinderdemo.util.Downloader
import com.iconfinderdemo.util.getProgressDrawable
import com.iconfinderdemo.util.loadImage
import com.iconfinderdemo.view.IconListActivity
import kotlinx.android.synthetic.main.item_icon.view.*
import kotlinx.android.synthetic.main.item_icon_set.view.*

class IconAdapter(var context: Context,var downloader: Downloader) : PagedListAdapter<Icon, IconAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Icon>() {
            override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean {
                return oldItem.icon_id == newItem.icon_id
            }
            override fun areContentsTheSame(oldItem: Icon, newItem: Icon): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_icon,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon = getItem(position)
        icon?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                downloader.donwload(icon.raster_sizes[6].formats[0].preview_url)
            }
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(icon:Icon)
        {
            val progressDrawable = getProgressDrawable(itemView.context)
            val previewUrl = icon.raster_sizes[6].formats[0].preview_url
            //Glide.with(itemView).load(previewUrl).into(itemView.iconIV)
            itemView.iconIV.loadImage(previewUrl,progressDrawable)
        }
    }
}