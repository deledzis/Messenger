package com.deledzis.messenger.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.deledzis.messenger.R
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.databinding.ItemChatBinding
import com.deledzis.messenger.util.extensions.autoNotify
import kotlin.properties.Delegates

class ChatsAdapter(private val controller: ChatItemActionsHandler) :
    RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    var chats: List<ChatReduced> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id == n.id && o.interlocutor == n.interlocutor }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemChatBinding>(
            inflater,
            R.layout.item_chat,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = chats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatReduced) = with(binding) {
            chat = item
            controller = this@ChatsAdapter.controller
        }
    }
}