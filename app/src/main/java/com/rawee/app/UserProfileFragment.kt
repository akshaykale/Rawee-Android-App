package com.rawee.app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AlertDialog
import android.view.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.facebook.login.LoginManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.rawee.app.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_profile.*


class UserProfileFragment : DialogFragment() {

    private val TAG = javaClass.simpleName

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window.attributes.windowAnimations = R.style.myDialog
        dialog.window.setGravity(Gravity.BOTTOM)

        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)

        val p: WindowManager.LayoutParams = dialog.window.attributes
        p.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = p
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUserName.text = FirebaseAuth.getInstance().currentUser!!.displayName
        tvUserEmail.text = FirebaseAuth.getInstance().currentUser!!.email ?: ""

        try {
            GlideApp.with(this)
                    .asBitmap()
                    .load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
                    .placeholder(R.drawable.profile_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : BitmapImageViewTarget(ivProfilePic) {
                        override fun setResource(resource: Bitmap?) {
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                            circularBitmapDrawable.isCircular = true
                            if (ivProfilePic != null) ivProfilePic.setImageDrawable(circularBitmapDrawable)
                        }
                    })
        } catch (ex:Exception){}
        btRate.setOnClickListener { onRateButtonClicked(view) }
        btShareApp.setOnClickListener { onShareAppButtonClicked(view) }
        bt_Signout.setOnClickListener{ onSignOutButtonClick(view)}
    }

    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(activity!!).setCurrentScreen(activity!!, "user_profile_menu", javaClass.simpleName) /** Analytics*/
    }

    private fun onSignOutButtonClick(view: View) {
        val builder = AlertDialog.Builder(context!!)
        builder.setPositiveButton("Sign-out", { dialog, id ->
            LoginManager.getInstance().logOut()
            RaweeApplication.getsInstance().googleApiHelperInstance.signOut({},{})
            FirebaseAuth.getInstance().signOut()
            //LocalDataStorageManager.getInstance().clear()
            //progressBar.setVisibility(View.GONE)
            startActivity(Intent(activity, SplashScreenActivity::class.java))
            activity!!.finish()
        })
        builder.setNegativeButton("Cancel", { dialog, id -> dialog.dismiss() })
        builder.setMessage("You will be signed out of app.")
        val dialog = builder.create()
        dialog.show()
    }

    private fun onShareAppButtonClicked(view: View?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + context!!.packageName)
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!")
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun onRateButtonClicked(view: View?) {
        val uri = Uri.parse("market://details?id=" + context!!.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        } else {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.packageName)))
        }
    }
}