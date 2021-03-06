package com.deledzis.messenger.data.mapper.chats

import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.entity.chats.Chats


class ChatsTestData {
    companion object {
        val chat = Chat(
            id = 3,
            title = "Третий",
            interlocutorId = 3,
            lastMessage = null
        )

        val chatEntity = ChatEntity(
            id = 3,
            title = "Третий",
            interlocutorId = 3,
            lastMessage = null
        )

        val chats = Chats(
            items = listOf(
                Chat(
                    id = 1,
                    title = "Первый",
                    interlocutorId = 2,
                    lastMessage = null
                ),
                Chat(
                    id = 2,
                    title = "Второй",
                    interlocutorId = 3,
                    lastMessage = null
                )
            )
        )

        val chatsEntity = ChatsEntity(
            items = listOf(
                ChatEntity(
                    id = 1,
                    title = "Первый",
                    interlocutorId = 2,
                    lastMessage = null
                ),
                ChatEntity(
                    id = 2,
                    title = "Второй",
                    interlocutorId = 3,
                    lastMessage = null
                )
            )
        )
    }
}