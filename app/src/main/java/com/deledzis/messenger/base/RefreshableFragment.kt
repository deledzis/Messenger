package com.deledzis.messenger.base

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class RefreshableFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    open lateinit var srl: SwipeRefreshLayout

    override fun onRefresh() {
        bindObservers()
    }
}