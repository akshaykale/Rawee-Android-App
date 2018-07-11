package com.rawee.app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rawee.app.models.MProduct
import com.rawee.app.utils.GlideApp
import com.rawee.app.utils.firebase.FireDataManager
import kotlinx.android.synthetic.main.layout_grid_item_view.view.*
import java.util.*

class HomeFragmentRecyclerViewAdapter(val context: Context, private val posts: TreeMap<Long, MProduct>, val listener: IRecyclerViewClickListner<MProduct>) :
        RecyclerView.Adapter<HomeFragmentRecyclerViewAdapter.GridRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridRecyclerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_item_view, parent, false)
        return GridRecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GridRecyclerViewHolder, position: Int) {
        val post = getElementByIndex(position) ?: return
        /*FireDataManager().imagesStorageReference.child(post.imageId).downloadUrl.addOnSuccessListener {
            try {
                GlideApp.with(context)
                        .load(it.toString().split("?")[0] + "?alt=media")
                        .override(300, 300)
                        .placeholder(Utils.getRandomPlaceholder())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.view.ivGridItemImage)
            }catch (ex:Exception){ ex.printStackTrace() }
        }*/
        try {
            GlideApp.with(context)
                    .load(post.thumb)
                    .override(300, 300)
                    .placeholder(R.drawable.ico_reading_96)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.view.thumb)
        }catch (ex:Exception){ ex.printStackTrace() }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    private fun getElementByIndex(index: Int) : MProduct?{
        return posts[posts.keys.toLongArray()[index]]
    }

    /** Memory Management*/
    /*override fun onViewRecycled(holder: GridRecyclerViewHolder) {
        holder.view.ivGridItemImage.setImageDrawable(null)
        super.onViewRecycled(holder)
    }
    *//** Memory Management*//*
    override fun onFailedToRecycleView(holder: GridRecyclerViewHolder): Boolean {
        return true
    }*/

    inner class GridRecyclerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { listener.onRecyclerViewItemClicked(it, getElementByIndex(adapterPosition)!!) }
        }
    }
}