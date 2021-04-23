package com.deledzis.messenger.presentation.features.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.infrastructure.extensions.autoNotify
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.databinding.ItemChatBinding
import kotlin.properties.Delegates

class ChatsAdapter(private val controller: ChatItemActionsHandler, private val userId: Int) :
    RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    var chats: List<Chat> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n ->
            o.id == n.id
                    && o.interlocutorId == n.interlocutorId
                    && o.title == n.title
                    && o.lastMessage == n.lastMessage
        }
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

    @VisibleForTesting()
    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) = with(binding) {
            chat = item
            userId = this@ChatsAdapter.userId
            controller = this@ChatsAdapter.controller
        }
    }
}