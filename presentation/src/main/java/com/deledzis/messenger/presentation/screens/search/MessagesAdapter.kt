package com.deledzis.messenger.presentation.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.infrastructure.extensions.autoNotify
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.databinding.ItemMessageBinding
import kotlin.properties.Delegates

class MessagesAdapter(private val userId: Int) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    var messages: List<Message> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new
        ) { o, n -> o.id == n.id && o.authorId == n.authorId && o.content == n.content }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemMessageBinding>(
            inflater,
            R.layout.item_message,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message) = with(binding) {
            message = item
            userId = this@MessagesAdapter.userId
        }
    }
}