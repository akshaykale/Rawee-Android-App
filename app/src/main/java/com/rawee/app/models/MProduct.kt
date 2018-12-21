package com.rawee.app.models

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

/**
 * Authors: /books/{bookId}/authors
 * Favourite: /books/{bookId}/rating  list of userIds
 *
 */

@IgnoreExtraProperties
class MProduct : MBaseModel(), Serializable {

    var bookName : String = ""
    var bookDesc : String = ""

    var thumb : String = ""
    var banner : String = ""

    var categories : HashMap<String, Boolean> = HashMap()

    var utcListing : Long = 0


    public override fun _isValid(): Boolean {
        if (bookName.isEmpty() || bookName.isBlank())
            return false
        if (thumb.isBlank() || thumb.isEmpty())
            return false
        if (banner.isEmpty() || thumb.isBlank())
            return false
        if (bookDesc.isEmpty() || bookDesc.isBlank())
            return false
        /*if (utc <= 0)
            return false*/

        return super._isValid()
    }

    public override fun _build() {

        EProductCategory.values().forEach {
            categories[it.name] = false
        }

        super._build()
    }

    enum class EProductCategory {
        ACTION,
        DRAMA,
        COMEDY,
        ANIME,
        KIDS,
        ROMANTIC,
        THRILLER,
        SCIENCE_FICTION,
        ADVENTURE,
        MYSTERY,
        HORROR,
        GUIDE,
        TRAVEL,
        SCIENCE,
        HISTORY,
        MATH,
        POETRY,
        COMICS,
        ART,
        COOKBOOKS,
        DIARIES,
        JOURNALS,
        BIOGRAPHIES,
        FANTASY,
        AUTOBIOGRAPHIES
    }
}