package com.deledzis.messenger.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.deledzis.messenger.R
import com.deledzis.messenger.util.DeferredFragmentTransaction
import com.deledzis.messenger.util.Loggable
import java.util.*

/**
 * The base class in which all the general logic for activity
 */
abstract class BaseActivity : AppCompatActivity(), LifecycleOwner,
    Loggable {
    private val queueDeferredFragmentTransactions = ArrayDeque<DeferredFragmentTransaction>()

    private var isActivityVisible = false

    private lateinit var fm: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fm = supportFragmentManager
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true

        // Perform all deferred fragment transitions
        while (queueDeferredFragmentTransactions.isNotEmpty()) {
            queueDeferredFragmentTransactions.remove().commit()
        }
    }

    public override fun onPause() {
        super.onPause()
        isActivityVisible = false
    }

    /**
     * Function that replaces current fragment with defined one
     *
     * @param fragment - preferred fragment
     * @param tag - string name of preferred fragment
     */
    fun setFragment(fragment: Fragment, tag: String) {
        if (isActivityVisible) {
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }

            fm.beginTransaction()
                .replace(R.id.main_container, fragment, tag)
                .commit()
        } else {
            // In case the activity is not running anymore,
            // add the fragment transaction to the deferred transaction
            val deferredFragmentTransaction = object : DeferredFragmentTransaction() {
                override fun commit() {
                    setFragment(fragment, tag)
                }
            }

            deferredFragmentTransaction.containerId = R.id.main_container
            deferredFragmentTransaction.fragment = fragment

            queueDeferredFragmentTransactions.add(deferredFragmentTransaction)
        }
    }

    fun replaceTopFragment(fragment: Fragment, tag: String) {
        if (isActivityVisible) {
            if (fm.backStackEntryCount > 0) {
                fm.popBackStack()
            }

            fm.beginTransaction()
                .add(R.id.main_container, fragment)
                .addToBackStack(tag)
                .commit()
        } else {
            // In case the activity is not running anymore,
            // add the fragment transaction to the deferred transaction
            val deferredFragmentTransaction = object : DeferredFragmentTransaction() {
                override fun commit() {
                    replaceTopFragment(fragment, tag)
                }
            }

            deferredFragmentTransaction.containerId = R.id.main_container
            deferredFragmentTransaction.fragment = fragment

            queueDeferredFragmentTransactions.add(deferredFragmentTransaction)
        }
    }

    /**
     * Function that adds preferred fragment on top of stack
     *
     * @param fragment - preferred fragment
     * @param tag - string name of preferred fragment
     */
    fun addFragment(fragment: Fragment, tag: String) {
        if (isActivityVisible) {
            fm.beginTransaction()
                .add(R.id.main_container, fragment)
                .addToBackStack(tag)
                .commit()
        } else {
            // In case the activity is not running anymore, add the fragment transaction to the deferred transaction
            val deferredFragmentTransaction = object : DeferredFragmentTransaction() {
                override fun commit() {
                    addFragment(fragment, tag)
                }
            }

            deferredFragmentTransaction.containerId = R.id.main_container
            deferredFragmentTransaction.fragment = fragment

            queueDeferredFragmentTransactions.add(deferredFragmentTransaction)
        }
    }

    /**
     * Function that removes current fragment from stack
     *
     * @param immediate - pops with different delay, true - immediately, false - just pops
     */
    fun removeFragment(immediate: Boolean = false) {
        if (isActivityVisible) {
            if (immediate) {
                fm.popBackStackImmediate()
            } else {
                fm.popBackStack()
            }
        } else {
            // In case the activity is not running anymore,
            // add the fragment transaction to the deferred transaction
            val deferredFragmentTransaction = object : DeferredFragmentTransaction() {
                override fun commit() {
                    removeFragment(immediate)
                }
            }
            queueDeferredFragmentTransactions.add(deferredFragmentTransaction)
        }
    }
}