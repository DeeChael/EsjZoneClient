package net.deechael.esjzone.item

import net.deechael.esjzone.item.content.Content

class DetailedChapter(title: String, contents: List<Content>) {

    val title: String
    val contents: List<Content>

    init {
        this.title = title
        this.contents = contents
    }

}