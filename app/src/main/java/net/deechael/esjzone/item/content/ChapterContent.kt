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

class ChapterBreakLine : ChapterContent {

    override fun toString(): String {
        return "\n"
    }

}

class ChapterImage(url: String) : ChapterContent {

    val url: String

    init {
        this.url = url
    }

    override fun toString(): String {
        return this.url
    }

}