package ru.kabylin.androidarchexample.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class RecyclerItemHolder<in T>(
    val context : Context,
    private val itemView : View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun initViews(data : T)
}
