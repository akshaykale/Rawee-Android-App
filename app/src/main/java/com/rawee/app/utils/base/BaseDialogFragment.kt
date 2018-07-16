package com.rawee.app.utils.base

import android.support.v4.app.DialogFragment

abstract class BaseDialogFragment: DialogFragment() {

    fun isValid() : Boolean{
        if (activity == null || context == null)
            return false
        return true
    }
}