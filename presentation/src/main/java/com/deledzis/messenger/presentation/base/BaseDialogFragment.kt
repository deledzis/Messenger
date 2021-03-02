package com.deledzis.messenger.presentation.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.deledzis.messenger.infrastructure.extensions.layoutInflater
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.features.main.MainActivity

abstract class BaseDialogFragment(@LayoutRes val layoutId: Int) : DialogFragment() {
    protected lateinit var activity: MainActivity
    protected abstract var dataBinding: ViewDataBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as MainActivity
    }

    override fun getTheme(): Int = R.style.AlertDialogTheme_Light

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            requireContext().layoutInflater,
            layoutId,
            container,
            false
        )
        dataBinding.lifecycleOwner = this

        bindObservers()

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    protected open fun bindObservers() {}
}