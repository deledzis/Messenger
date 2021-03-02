package com.deledzis.messenger.presentation.features.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.deledzis.messenger.common.extensions.daysBetween
import com.deledzis.messenger.common.extensions.formatForChat
import com.deledzis.messenger.common.util.DateUtils
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.domain.model.entity.messages.Message.Companion.TYPE_TEXT
import com.deledzis.messenger.infrastructure.extensions.autoNotify
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.databinding.ItemFileMessageInterlocutorBinding
import com.deledzis.messenger.presentation.databinding.ItemFileMessageUserBinding
import com.deledzis.messenger.presentation.databinding.ItemTextMessageInterlocutorBinding
import com.deledzis.messenger.presentation.databinding.ItemTextMessageUserBinding
import java.util.*
import kotlin.properties.Delegates

class MessagesAdapter(private val userId: Int, private val controller: MessageItemActionsHandler) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TEXT_MESSAGE_FROM_USER = 0
        const val TYPE_TEXT_MESSAGE_FROM_INTERLOCUTOR = 1
        const val TYPE_FILE_MESSAGE_FROM_USER = 2
        const val TYPE_FILE_MESSAGE_FROM_INTERLOCUTOR = 3
    }

    var messages: List<Message> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n ->
            o.id == n.id && o.type == n.type && o.authorId == n.authorId && o.content == n.content
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.authorId == userId) {
            when (message.type) {
                TYPE_TEXT -> TYPE_TEXT_MESSAGE_FROM_USER
                else -> TYPE_FILE_MESSAGE_FROM_USER
            }
        } else {
            when (message.type) {
                TYPE_TEXT -> TYPE_TEXT_MESSAGE_FROM_INTERLOCUTOR
                else -> TYPE_FILE_MESSAGE_FROM_INTERLOCUTOR
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TEXT_MESSAGE_FROM_USER -> {
                val binding = DataBindingUtil.inflate<ItemTextMessageUserBinding>(
                    inflater,
                    R.layout.item_text_message_user,
                    parent,
                    false
                )
                TextMessageFromUserViewHolder(binding)
            }
            TYPE_TEXT_MESSAGE_FROM_INTERLOCUTOR -> {
                val binding = DataBindingUtil.inflate<ItemTextMessageInterlocutorBinding>(
                    inflater,
                    R.layout.item_text_message_interlocutor,
                    parent,
                    false
                )
                TextMessageFromInterlocutorViewHolder(binding)
            }
            TYPE_FILE_MESSAGE_FROM_USER -> {
                val binding = DataBindingUtil.inflate<ItemFileMessageUserBinding>(
                    inflater,
                    R.layout.item_file_message_user,
                    parent,
                    false
                )
                FileMessageFromUserViewHolder(binding)
            }
            else -> {
                val binding = DataBindingUtil.inflate<ItemFileMessageInterlocutorBinding>(
                    inflater,
                    R.layout.item_file_message_interlocutor,
                    parent,
                    false
                )
                FileMessageFromInterlocutorViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val previousMessage = if (position < messages.size - 1) messages[position + 1] else null
        val date = if (previousMessage == null) {
            DateUtils.getDate(message.date)?.formatForChat()
        } else {
            val messageDate = DateUtils.getDate(message.date) ?: return
            val previousMessageDate = DateUtils.getDate(previousMessage.date) ?: return
            if (messageDate.daysBetween(previousMessageDate) > 0L) {
                messageDate.formatForChat()
            } else {
                null
            }
        }
        if (message.authorId == userId) {
            when (message.type) {
                TYPE_TEXT -> (holder as TextMessageFromUserViewHolder).bind(
                    item = message,
                    date = date,
                )
                else -> (holder as FileMessageFromUserViewHolder).bind(
                    item = message,
                    date = date,
                )
            }
        } else {
            when (message.type) {
                TYPE_TEXT -> (holder as TextMessageFromInterlocutorViewHolder).bind(
                    item = message,
                    date = date,
                )
                else -> (holder as FileMessageFromInterlocutorViewHolder).bind(
                    item = message,
                    date = date,
                )
            }
        }
    }

    inner class TextMessageFromUserViewHolder(private val binding: ItemTextMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message, date: String? = null) = with(binding) {
            message = item
            controller = this@MessagesAdapter.controller
            this.date = date
        }
    }

    inner class TextMessageFromInterlocutorViewHolder(private val binding: ItemTextMessageInterlocutorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message, date: String? = null) = with(binding) {
            message = item
            controller = this@MessagesAdapter.controller
            this.date = date
        }
    }

    inner class FileMessageFromUserViewHolder(private val binding: ItemFileMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message, date: String? = null) = with(binding) {
            message = item
            controller = this@MessagesAdapter.controller
            this.date = date
        }
    }

    inner class FileMessageFromInterlocutorViewHolder(private val binding: ItemFileMessageInterlocutorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message, date: String? = null) = with(binding) {
            message = item
            controller = this@MessagesAdapter.controller
            this.date = date
        }
    }
}