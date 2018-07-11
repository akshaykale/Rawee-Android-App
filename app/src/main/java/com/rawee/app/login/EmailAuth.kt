package com.rawee.app.login

import com.google.firebase.auth.ActionCodeSettings
import java.util.regex.Pattern

class EmailAuth {

    companion object {
        val ISO_BUNDLE : String = "com.rawee.app"
        val ANDROID_BUNDLE : String = "com.rawee.app"
        val URL : String = "com.rawee.app"

        fun getEmailFormat() : ActionCodeSettings {
            val actionCodeSettings = ActionCodeSettings.newBuilder()
                    // URL you want to redirect back to. The domain (www.example.com) for this
                    // URL must be whitelisted in the Firebase Console.
                    .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                    // This must be true
                    .setHandleCodeInApp(true)
                    .setIOSBundleId("com.example.ios")
                    .setAndroidPackageName(
                            ANDROID_BUNDLE,
                            true, /* installIfNotAvailable */
                            12.toString()    /* minimumVersion */)
                    .build()
            return actionCodeSettings
        }

        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }
    }
}