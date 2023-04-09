package net.deechael.esjzone.client.tag

import net.deechael.esjzone.client.EsjzoneClient
import net.deechael.esjzone.client.novel.Novel
import net.deechael.esjzone.client.types.Sorts
import net.deechael.esjzone.client.types.Types
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

class Tag(private val client: EsjzoneClient, val name: String) {

    /**
     * 列出小说
     * @param type 小说类型
     * @param sort 筛选方法
     * @return 获得到的小说列表
     */
    fun listNovels(type: Types = Types.ALL, sort: Sorts = Sorts.RECENTLY_UPDATED): List<Novel> {
        val novels = mutableListOf<Novel>()
        val document =
            this.client.service.getNovelsByTag(type.index, sort.index, this.name).execute().body()!!
        val rawCovers =
            Xsoup.select(
                document,
                "/html/body/div[3]/section/div/div[1]/div[3]/div/div/a/div/div/div"
            ).elements
        val rawInfos = Xsoup.select(
            document,
            "/html/body/div[3]/section/div/div[1]/div[3]/div/div/div/h5/a"
        ).elements
        for (i in 0 until rawCovers.size) {
            val rawCoverUrl = rawCovers[i].attr("data-src")
            val rawUrl = rawInfos[i].attr("href")
            novels.add(
                Novel(
                    client = this.client,
                    id = rawUrl.substring(8, rawUrl.length - 5),
                    name = rawInfos[i].text(),
                    cover = if (rawCoverUrl.startsWith("/assets")) "https://www.esjzone.cc$rawCoverUrl" else rawCoverUrl
                )
            )
        }
        return novels.toImmutableList()
    }

}