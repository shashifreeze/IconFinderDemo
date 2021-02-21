package com.iconfinderdemo.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iconfinderdemo.R
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.util.Constants.KEY_ICON_SET_ID
import com.iconfinderdemo.view.IconListActivity
import kotlinx.android.synthetic.main.item_icon_set.view.*

class IconSetAdapter(var context: Context) : PagedListAdapter<IconSet, IconSetAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<IconSet>() {
            override fun areItemsTheSame(oldItem: IconSet, newItem: IconSet): Boolean {
                return oldItem.iconSetId == newItem.iconSetId
            }

            override fun areContentsTheSame(oldItem: IconSet, newItem: IconSet): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_icon_set,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val iconSet = getItem(position)
        iconSet?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, IconListActivity::class.java)
                intent.putExtra(KEY_ICON_SET_ID, iconSet.iconSetId)
                context.startActivity(intent)
            }
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(iconSet:IconSet)
        {
            itemView.iconSetNameTV.text = iconSet.name
            itemView.iconCountTV.text = iconSet.iconsCount.toString()
        }

    }

}