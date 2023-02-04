package net.deechael.esjzone.item.content

interface ChapterContent

class ChapterText(content: String) : ChapterContent {

    val content: String

    init {
        this.content = content
    }

    override fun toString(): String {
        return this.content
    }

}

class ChatperBreakLine : ChapterContent {

    override fun toString(): String {
        return "\n"
    }

}

class ChatperImage(url: String) : ChapterContent {

    val url: String

    init {
        this.url = url
    }

    override fun toString(): String {
        return this.url
    }

}