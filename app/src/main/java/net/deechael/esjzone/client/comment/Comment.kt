package net.deechael.esjzone.client.comment

import net.deechael.esjzone.client.EsjzoneClient
import net.deechael.esjzone.client.chapter.Chapter
import net.deechael.esjzone.client.novel.Novel
import net.deechael.esjzone.client.user.User

open class Comment(
    internal val client: EsjzoneClient,
    val novel: Novel,
    val chapter: Chapter?,
    val id: String,
    val senderId: Int,
    val content: String,
    val replayContent: String? = null
) {

    private var sender: User? = null

    fun reply(content: String) {
        if (this.chapter == null) {
            // this.client.service.replyComment(BodyBuilder.of()
            //     .param("content", content)
            //     .param("reply", "${this.id}-${this.senderId}")
            //     .param("forum_id", "0")
            //     .param("data", "books")
            //     .build(), this.client.getAuthToken("detail/${novel.id}.html"))
            this.client.service.replyComment(
                this.client.getAuthToken("detail/${novel.id}.html"),
                content,
                "${this.id}-${this.senderId}"
            )
        } else {
            // this.client.service.replyComment(BodyBuilder.of()
            //     .param("content", content)
            //     .param("reply", "${this.id}-${this.senderId}")
            //     .param("forum_id", this.chapter.id)
            //     .param("data", "forum")
            //     .build(), this.client.getAuthToken("forum/${novel.id}/${chapter.id}.html"))
            this.client.service.replyComment(
                this.client.getAuthToken("forum/${novel.id}/${chapter.id}.html"),
                content,
                "${this.id}-${this.senderId}",
                this.chapter.id,
                "forum"
            )
        }
    }

    fun getSender(): User {
        if (this.sender == null)
            this.sender = this.client.getUserInfo(this.senderId)
        return this.sender!!
    }

}

class MeComment(
    client: EsjzoneClient,
    novel: Novel,
    chapter: Chapter?,
    id: String,
    senderId: Int,
    content: String,
    replayContent: String? = null
) : Comment(client, novel, chapter, id, senderId, content, replayContent) {

    fun delete() {
        if (this.chapter == null) {
            // this.client.service.deleteComment(BodyBuilder.of()
            //     .param("rid", this.id)
            //     .param("type", "book")
            //     .param("data", "books")
            //     .build(), this.client.getAuthToken("detail/${novel.id}.html"))
            this.client.service.deleteComment(
                this.client.getAuthToken("detail/${novel.id}.html"),
                this.id
            )
        } else {
            // this.client.service.deleteComment(BodyBuilder.of()
            //     .param("rid", this.id)
            //     .param("type", "forum")
            //     .param("data", "forum")
            //     .build(), this.client.getAuthToken("forum/${novel.id}/${chapter.id}.html"))
            this.client.service.deleteComment(
                this.client.getAuthToken("forum/${novel.id}/${chapter.id}.html"),
                this.id,
                "forum",
                "forum"
            )
        }
    }
}