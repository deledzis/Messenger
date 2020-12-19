package com.deledzis.messenger.ui.addchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.deledzis.messenger.R
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.databinding.ItemUserBinding
import com.deledzis.messenger.util.extensions.autoNotify
import kotlin.properties.Delegates

class UsersAdapter(private val controller: UserItemActionsHandler) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    var users: List<User> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new
        ) { o, n -> o.id == n.id && o.username == n.username && o.nickname == n.nickname }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            inflater,
            R.layout.item_user,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) = with(binding) {
            user = item
            controller = this@UsersAdapter.controller
        }
    }
}