package com.deledzis.messenger.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.deledzis.messenger.R
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.databinding.*
import com.deledzis.messenger.util.DateUtils
import com.deledzis.messenger.util.DateUtils.DF_ONLY_DAY
import com.deledzis.messenger.util.extensions.*
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
            o.id == n.id && o.type == n.type && o.author == n.author && o.content == n.content
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.author.id == userId) {
            when (message.type) {
                true -> TYPE_FILE_MESSAGE_FROM_USER
                false -> TYPE_TEXT_MESSAGE_FROM_USER
            }
        } else {
            when (message.type) {
                true -> TYPE_FILE_MESSAGE_FROM_INTERLOCUTOR
                false -> TYPE_TEXT_MESSAGE_FROM_INTERLOCUTOR
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
            when {
                DateUtils.getDate(message.date).isToday() -> "Сегодня"
                DateUtils.getDate(message.date).isYesterday() -> "Вчера"
                else -> DateUtils.getDate(message.date).formatDate(format = DF_ONLY_DAY)
            }
        } else {
            val messageDate = DateUtils.getDate(message.date)
            val previousMessageDate = DateUtils.getDate(previousMessage.date)
            if (messageDate.daysBetween(previousMessageDate) > 0L) {
                when {
                    messageDate.isToday() -> "Сегодня"
                    messageDate.isYesterday() -> "Вчера"
                    else -> messageDate.formatDate(format = DF_ONLY_DAY)
                }
            } else {
                null
            }
        }
        if (message.author.id == userId) {
            when (message.type) {
                true -> (holder as FileMessageFromUserViewHolder).bind(
                    item = message,
                    date = date,
                )
                false -> (holder as TextMessageFromUserViewHolder).bind(
                    item = message,
                    date = date,
                )
            }
        } else {
            when (message.type) {
                true -> (holder as FileMessageFromInterlocutorViewHolder).bind(
                    item = message,
                    date = date,
                )
                false -> (holder as TextMessageFromInterlocutorViewHolder).bind(
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