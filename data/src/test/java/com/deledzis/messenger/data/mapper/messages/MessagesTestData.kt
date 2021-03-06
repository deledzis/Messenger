package com.deledzis.messenger.data.mapper.messages

import com.deledzis.messenger.data.model.messages.MessageEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.domain.model.entity.messages.Messages

class MessagesTestData {
    companion object {
        val messagesEntity = MessagesEntity(
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

        val messages = Messages(
            items = listOf(
                Message(
                    id = 1,
                    type = 0,
                    content = "blabla",
                    date = "11/02/2021",
                    chatId = 1,
                    authorId = 1,
                    authorName = "Igorek"
                ),
                Message(
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

        val messageEntity = MessageEntity(
            id = 2,
            type = 0,
            content = "blablabla",
            date = "11/02/2021",
            chatId = 1,
            authorId = 2,
            authorName = "Vovan"
        )

        val message = Message(
            id = 2,
            type = 0,
            content = "blablabla",
            date = "11/02/2021",
            chatId = 1,
            authorId = 2,
            authorName = "Vovan"
        )
    }
}