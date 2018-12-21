package com.rawee.app.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.rawee.app.R
import com.rawee.app.models.MAuthor
import com.rawee.app.models.MProduct
import com.rawee.app.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_book_details.*
import java.io.File.separator
import java.util.concurrent.TimeUnit

class BookDetailsActivity : AppCompatActivity(), OnSuccessListener<DocumentSnapshot> {

    private lateinit var product: MProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_book_details)

        val productId = intent.getStringExtra("product_id")
        if (productId == null || productId.isBlank() || productId.isEmpty()) {
            product = intent.getSerializableExtra("product") as MProduct
            initUI()
        } else {
            FirebaseFirestore.getInstance()
                    .collection("products")
                    .document(productId)
                    .get()
                    .addOnSuccessListener(this)
                    .addOnFailureListener { showErrorDialog() }
        }

        //Menu

    }

    /**
     * Loaded the product from DB using productId
     */
    override fun onSuccess(snapshot: DocumentSnapshot?) {
        if (snapshot != null) {
            val p = snapshot.toObject(MProduct::class.java)
            if (p != null && p._isValid()) {
                product = p
                initUI()
            } else {
                showErrorDialog()
            }
        } else {
            // invalid book id
        }
    }

    private fun initUI() {

        try {
            GlideApp.with(applicationContext)
                    .load(product.banner)
                    .placeholder(R.drawable.ico_reading_96)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProductDetailsBannerImage)
        }catch (ex:Exception){ ex.printStackTrace() }

        tvProductDetailsTitle.text = product.bookName
        tvProductDetailsDesc.text = product.bookDesc
        tvProductDetailsCategory.text = product.categories.filterValues { it == true }.keys.toString()
        //authors
        FirebaseFirestore.getInstance().collection("products").document(product.id.toString()).collection("authors").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null){ return@addSnapshotListener }
            val authors : ArrayList<String> = ArrayList()
            if (querySnapshot != null){
                for (dc in querySnapshot.documentChanges){
                    val author = dc.document.toObject(MAuthor::class.java)
                    when {
                        dc.type == DocumentChange.Type.ADDED -> {
                            authors.add(author.displayName)
                        }
                    }
                }
                tvProductDetailsAuthors.text = authors.get(0)
            }
        }
        //favourites
        FirebaseFirestore.getInstance().collection("products").document(product.id.toString()).collection("favourites").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null){ return@addSnapshotListener }
            val authors : ArrayList<String> = ArrayList()
            if (querySnapshot != null){
                tvProductDetailsFavourites.text = querySnapshot.documentChanges.size.toString()
            }
        }
        if (getDateDiff(product.utc_time, System.currentTimeMillis(), TimeUnit.DAYS) < 7){
            cardProductDetailsInfo.visibility = View.VISIBLE
            tvProductDetailsInfoText.text = "NEW"
        } else {
            cardProductDetailsInfo.visibility = View.GONE
        }
    }

    private fun showErrorDialog() {

    }

    fun getDateDiff(timeUpdate: Long, timeNow: Long, timeUnit: TimeUnit): Long {
        val diffInMillies = Math.abs(timeNow - timeUpdate)
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }
}