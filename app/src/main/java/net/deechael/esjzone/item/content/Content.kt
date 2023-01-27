package net.deechael.esjzone.item.content

interface Content

class Text(content: String) : Content {

    val content: String

    init {
        this.content = content
    }

    override fun toString(): String {
        return this.content
    }

}

class BreakLine : Content {

    override fun toString(): String {
        return "\n"
    }

}

class Image(url: String) : Content {

    val url: String

    init {
        this.url = url
    }

    override fun toString(): String {
        return this.url
    }

}