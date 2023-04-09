package net.deechael.esjzone.client.category

import net.deechael.esjzone.client.EsjzoneClient
import net.deechael.esjzone.client.novel.Novel
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

/**
 * 小说分类
 *
 * @param client Esjzone客户端
 * @param id 分类id
 * @param name 分类名称
 */
class Category(val client: EsjzoneClient, val id: String, val name: String) {

    /**
     * 列出分类下的所有小说
     * @return 该分类下所有小说
     */
    fun listNovels(): List<Novel> {
        val novels = mutableListOf<Novel>()
        val document = this.client.service.getCategoryNovels(this.id).execute().body()!!
        for (element in Xsoup.select(
            document,
            "/html/body/div[3]/section/div/div/div/table/tbody/tr/td/a"
        ).elements) {
            val rawUrl = element.attr("href")
            val id = rawUrl.substring("/forum/${this.id}/".length, rawUrl.length - 1)
            novels.add(
                Novel(
                    this.client,
                    id,
                    element.text()
                )
            )
        }
        return novels.toImmutableList()
    }

}