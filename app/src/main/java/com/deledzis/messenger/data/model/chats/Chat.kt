package com.deledzis.messenger.data.model.chats

import com.deledzis.messenger.data.model.user.User

data class Chat(
    val id: Int,
    val interlocutor: User,
    val messages: List<Message>
)

data class ChatReduced(
    val id: Int,
    val interlocutor: User,
    val lastMessage: Message
)