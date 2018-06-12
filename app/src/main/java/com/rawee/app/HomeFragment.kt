package com.rawee.app

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.firebase.auth.FirebaseAuth
import com.rawee.app.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                            circularBitmapDrawable.isCircular = true
                            if (btUserProfile != null) btUserProfile.setImageDrawable(circularBitmapDrawable)
                        }
                    })
        } catch (ex: Exception) { ex.printStackTrace()
        }
    }
}