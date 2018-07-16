package com.rawee.app

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.firebase.auth.FirebaseAuth
import com.rawee.app.models.MProduct
import com.rawee.app.utils.GlideApp
import com.rawee.app.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : BaseFragment(), IRecyclerViewClickListner<MProduct> {

    private val posts: TreeMap<Long, MProduct> = TreeMap(Comparator<Long> { o1, o2 -> o2!!.compareTo(o1!!) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //posts.put(11, MProduct());
        posts.put(12, MProduct("https://media.bloomsbury.com/rep/bj/9781408855706.jpeg"));
        posts.put(11, MProduct("https://images-na.ssl-images-amazon.com/images/I/51Z0D249VEL._SX258_BO1,204,203,200_.jpg"));
        posts.put(13, MProduct("https://img.bookfrom.net/img/1707071406/1299_harry_potter_and_the_prisoner_of_azkaban.jpg"));
        posts.put(14, MProduct("https://i.pinimg.com/736x/76/4c/ac/764caccbc603fa980b1007327b2dbac4--ebook-cover-harry-potter-book-covers.jpg"));
        posts.put(19, MProduct("https://www.henrybear.com/components/com_virtuemart/shop_image/product/full/51Yuiikei3L589d02e6eeb37.jpg"));
        posts.put(18, MProduct("https://s-media-cache-ak0.pinimg.com/originals/25/b4/79/25b479276c59fe2b0d265bb6bfa36aa2.jpg"));
        posts.put(17, MProduct("http://www.bookcoverideas.com/wp-content/uploads/2012/09/famous-01.jpg"));


        if (!isValid()) return

        setUpRecyclerView()

        btUserProfile.setOnClickListener {
            val userProfileFragment = UserProfileFragment()
            userProfileFragment.show(activity!!.supportFragmentManager, userProfileFragment.javaClass.simpleName)
        }

        try {
            GlideApp.with(context!!)
                    .asBitmap()
                    .load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
                    .placeholder(R.drawable.profile_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : BitmapImageViewTarget(btUserProfile) {
                        override fun setResource(resource: Bitmap?) {
                            if (resource != null) {
                                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                                circularBitmapDrawable.isCircular = true
                                if (btUserProfile != null) btUserProfile.setImageDrawable(circularBitmapDrawable)
                            }
                        }
                    })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerViewHome.layoutManager = gridLayoutManager

        val adapter: HomeFragmentRecyclerViewAdapter = HomeFragmentRecyclerViewAdapter(context!!, posts, this)
        recyclerViewHome.adapter = adapter
    }

    override fun onRecyclerViewItemClicked(view: View, result: MProduct) {
        startActivity(Intent(activity, BookDetailsActivity::class.java))
    }
}