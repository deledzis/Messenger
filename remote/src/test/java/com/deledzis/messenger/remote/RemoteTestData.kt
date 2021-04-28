package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.data.model.messages.MessageEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity

class RemoteTestData {

    companion object {
        val auth = AuthEntity(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
            accessToken = "123"
        )

        val chats = ChatsEntity(
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

        val chat = ChatEntity(
            id = 3,
            title = "Третий",
            interlocutorId = 3,
            lastMessage = null
        )

        val serverMessageResponse = ServerMessageResponseEntity(
            errorCode = 0,
            message = null
        )

        val messages = MessagesEntity(
            items = listOf(
                MessageEntity(
                    id = 1,
                    type = 0,
                    content = "blabla",
                    date = "11/02/2021",
                    chatId = 1,
                    authorId = 1,
                    authorName = "Igorek"
                ),
                MessageEntity(
                    id = 2,
                    type = 0,
                    content = "blablabla",
                    date = "11/02/2021",
                    chatId = 1,
                    authorId = 2,
                    authorName = "Vovan"
                )
            )
        )

        val user = UserEntity(
            id = 1,
            username = "Igor",
            nickname = "igorek"
        )

        val users = UsersEntity(
            items = listOf(
                UserEntity(
                    id = 1,
                    username = "Igor",
                    nickname = "igorek"
                ),
                UserEntity(
                    id = 2,
                    username = "Vova",
                    nickname = "Vovan"
                )
            )
        )
    }
}