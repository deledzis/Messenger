package com.deledzis.messenger.presentation.base

import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerAppCompatActivity

/**
 * The base class for activity
 */
abstract class BaseActivity<T : ViewModel> : DaggerAppCompatActivity()