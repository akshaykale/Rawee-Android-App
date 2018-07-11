package com.rawee.app

import android.view.View

interface IRecyclerViewClickListner<T> {
    fun onRecyclerViewItemClicked(view: View, result:T)
}
