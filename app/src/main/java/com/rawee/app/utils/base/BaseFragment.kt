package com.rawee.app.utils.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    fun isValid() : Boolean {
        if (activity == null || context == null)
            return false
        return true
    }
}