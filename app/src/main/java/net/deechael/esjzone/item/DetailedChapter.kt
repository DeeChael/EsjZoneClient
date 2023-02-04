package net.deechael.esjzone.item

import net.deechael.esjzone.item.content.ChapterContent

class DetailedChapter(title: String, chapterContents: List<ChapterContent>) {

    val title: String
    val chapterContents: List<ChapterContent>

    init {
        this.title = title
        this.chapterContents = chapterContents
    }

}