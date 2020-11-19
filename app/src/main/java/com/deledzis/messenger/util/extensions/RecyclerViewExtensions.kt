package com.deledzis.messenger.util.extensions

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun <T> RecyclerView.Adapter<*>.autoNotify(o: List<T>, n: List<T>, compare: (T, T) -> Boolean) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(opos: Int, npos: Int): Boolean {
            return compare(o[opos], n[npos])
        }
        override fun areContentsTheSame(opos: Int, npos: Int): Boolean {
            return o[opos] == n[npos]
        }
        override fun getOldListSize() = o.size
        override fun getNewListSize() = n.size
    })
    diff.dispatchUpdatesTo(this)
}