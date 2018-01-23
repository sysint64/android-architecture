package ru.kabylin.androidarchexample.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open class ItemRecyclerAdapter<T>(
    protected val context: Context,
    val layout: Int,
    private val items: List<T>,
    private val holderFactory: (context: Context, view: View) -> RecyclerItemHolder<T>
) : RecyclerView.Adapter<RecyclerItemHolder<T>>() {
    open fun obtainLayout(viewType: Int): Int {
        return layout
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerItemHolder<T>?, position: Int) {
        holder?.initViews(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerItemHolder<T> {
        val view: View
        val inflater = LayoutInflater.from(parent?.context)

        view = inflater.inflate(obtainLayout(viewType), parent, false)
        return holderFactory(context, view)
    }
}
