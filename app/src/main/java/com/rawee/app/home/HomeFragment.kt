package com.rawee.app.home

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.rawee.app.IRecyclerViewClickListner
import com.rawee.app.R
import com.rawee.app.UserProfileFragment
import com.rawee.app.models.MAuthor
import com.rawee.app.models.MProduct
import com.rawee.app.utils.GlideApp
import com.rawee.app.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.HashMap

class HomeFragment : BaseFragment(), IRecyclerViewClickListner<MProduct>, EventListener<QuerySnapshot> {

    lateinit var adapter: HomeFragmentRecyclerViewAdapter
    private val posts: TreeMap<Long, MProduct> = TreeMap(Comparator<Long> { o1, o2 -> o2!!.compareTo(o1!!) })

    val urls = arrayOf("https://media.bloomsbury.com/rep/bj/9781408855706.jpeg",
            "https://images-na.ssl-images-amazon.com/images/I/51Z0D249VEL._SX258_BO1,204,203,200_.jpg",
            "https://img.bookfrom.net/img/1707071406/1299_harry_potter_and_the_prisoner_of_azkaban.jpg",
            "https://i.pinimg.com/736x/76/4c/ac/764caccbc603fa980b1007327b2dbac4--ebook-cover-harry-potter-book-covers.jpg",
            "https://www.henrybear.com/components/com_virtuemart/shop_image/product/full/51Yuiikei3L589d02e6eeb37.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/25/b4/79/25b479276c59fe2b0d265bb6bfa36aa2.jpg",
            "http://www.bookcoverideas.com/wp-content/uploads/2012/09/famous-01.jpg")

    val banners = arrayOf("https://png.pngtree.com/thumb_back/fw800/back_pic/03/95/63/1257ed385eb8a9c.jpg",
            "http://www.f-covers.com/cover/abstract-book-facebook-cover-timeline-banner-for-fb.jpg", "https://www.themarysue.com/wp-content/uploads/2015/10/cursed-child-stage-play-eighth-story.jpg",
            "http://flipthetruck.files.wordpress.com/2010/11/banner-potter7.png", "https://1.bp.blogspot.com/-BIgnUD0R0sU/V8uzs4BXigI/AAAAAAAAKKw/nW5ZEcPs49cuNOXRaTJ0jlnyX7qalr3hgCLcB/s1600/facebook%2Bbanner%2BVC.jpg")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //_addProducts()

        if (!isValid()) return

        setUpRecyclerView()

        val fProducts = FirebaseFirestore.getInstance().collection("products")
        fProducts.orderBy("utc_time", Query.Direction.DESCENDING).addSnapshotListener(this)

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

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) + start

    private fun _addProducts() {
        for (i in 1..10) {
            val ref = FirebaseFirestore.getInstance().collection("products").document()
            val p = MProduct()
            p._build()
            p.bookName = "Name of Book " + 1
            p.bookDesc = "Harry Potter is a series of fantasy novels written by British author J. K. Rowling. The novels chronicle the life of a young wizard, Harry Potter, and his friends Hermione Granger and Ron Weasley, all of whom are students at Hogwarts School of Witchcraft and Wizardry. The main story arc concerns Harry's struggle against Lord Voldemort, a dark wizard who intends to become immortal, overthrow the wizard governing body known as the Ministry of Magic, and subjugate all wizards and Muggles (non-magical people)."
            p.categories[MProduct.EProductCategory.ACTION.name] = true
            p.thumb = urls[(0..6).random()]
            p.banner = banners[(0..4).random()]
            p.id = ref.id
            ref.set(p)

            val authorRef = ref.collection("authors").document()
            val a = MAuthor()
            a.id = authorRef.id
            a.displayName = "Author Display Name"
            a.firstName = "First"
            a.lastName = "Last"
            a.emailId = "email@id.com"
            authorRef.set(a)

            val map = HashMap<String, Boolean>()
            map["userid1"] = true
            map["userId2"] = false
            ref.collection("favourites").document().set(map as Map<String, Any>)

        }
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerViewHome.layoutManager = gridLayoutManager

        adapter = HomeFragmentRecyclerViewAdapter(context!!, posts, this)
        recyclerViewHome.adapter = adapter
    }

    override fun onRecyclerViewItemClicked(view: View, result: MProduct) {
        val intent = Intent(activity, BookDetailsActivity::class.java)
        intent.putExtra("product", result)
        startActivity(intent)
    }

    override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            return
        }
        if (snapshot != null) {
            for (dc in snapshot.documentChanges) {
                val docSnap: DocumentSnapshot = dc.document
                val product = docSnap.toObject(MProduct::class.java)
                if (product == null || !product._isValid()) break
                when {
                    dc.type == DocumentChange.Type.ADDED -> {
                        posts[product.utc_time] = product
                        if (adapter != null) adapter.notifyDataSetChanged()
                    }
                    dc.type == DocumentChange.Type.MODIFIED -> {

                    }
                    dc.type == DocumentChange.Type.REMOVED -> {

                    }
                }
            }
        }
    }
}