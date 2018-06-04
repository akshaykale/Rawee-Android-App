package com.rawee.app.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.rawee.app.models.MUser
import com.rawee.app.utils.Logger

class FireDataManager() {

    private val TAG = javaClass.simpleName

    private val key_timestamp = "utc_time"

    private val userCollReference: CollectionReference = FirebaseFirestore.getInstance().collection("users")

    private var userId: String? = null

    init {
        if (FirebaseAuth.getInstance().currentUser != null)
            userId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun saveUser(user: FirebaseUser) {
        val userRef = userCollReference.document(user.uid)
        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    // User exists. Change the values
                    userRef.update("last_login", System.currentTimeMillis())
                } else {
                    addUser(user)
                }
            } else {
                addUser(user)
            }
        }
    }

    private fun addUser(user: FirebaseUser) {
        //construct user
        val mUser = MUser()
        mUser.display_name = user.displayName
        mUser.email = user.email
        mUser.id = user.uid
        mUser.photo_url = user.photoUrl.toString()
        val documentReference = userCollReference.document(mUser.id)
        documentReference.set(mUser).addOnSuccessListener { Logger.getsInstance().logD(TAG, "Success") }.addOnFailureListener { e -> Logger.getsInstance().logD(TAG, "Failed: ==" + e.message) }
    }

}
