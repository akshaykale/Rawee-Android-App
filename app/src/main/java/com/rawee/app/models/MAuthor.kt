package com.rawee.app.models

class MAuthor : MBaseModel() {

    var firstName : String = ""
    var lastName : String = ""
    var displayName : String = ""
    var emailId : String = ""

    override fun _isValid(): Boolean {
        if (firstName.isBlank() || firstName.isEmpty())
            return false
        if (lastName.isBlank() || lastName.isEmpty())
            return false
        if (displayName.isBlank() || displayName.isEmpty())
            return false
        if (emailId.isBlank() || emailId.isEmpty())
            return false
        return super._isValid()
    }

    override fun _build() {
        super._build()
    }
}