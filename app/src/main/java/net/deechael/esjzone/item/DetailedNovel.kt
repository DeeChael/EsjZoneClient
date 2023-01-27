package net.deechael.esjzone.item

class DetailedNovel(name: String, cover: String, url: String, chapters: List<Chapter>) :
    Novel(name, url) {

    val cover: String
    val chapters: List<Chapter>

    init {
        this.cover = cover
        this.chapters = chapters
    }

}