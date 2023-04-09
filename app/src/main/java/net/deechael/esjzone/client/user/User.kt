package net.deechael.esjzone.client.user

import android.annotation.SuppressLint
import net.deechael.esjzone.client.EsjzoneClient
import net.deechael.esjzone.client.novel.Novel
import okhttp3.internal.toImmutableList
import org.jsoup.nodes.Document
import us.codecraft.xsoup.Xsoup
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
internal val REGISTERED_TIME_FORMAT = SimpleDateFormat("註冊日期 yyyy-MM-dd")
internal val INT_REGEX = "-?\\d+".toRegex()
internal val DOUBLE_REGEX = "-?\\d+(\\.\\d+)?".toRegex()

open class User constructor(internal val client: EsjzoneClient, document: Document) {

    val uid = Xsoup.select(
        document,
        "/html/body/div[3]/section/div/div[2]/form/div[1]/div/label/a/text()"
    ).get()
        .split(" ")[1].toInt()

    val avatar =
        Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/aside/form/div[1]/img/@src")
            .get()
    val username =
        Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/aside/form/div[2]/h4/text()")
            .get()
    val exp =
        Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/aside/div/div/text()").get()
            .split(" ")[0].toInt()

    val registeredAt = REGISTERED_TIME_FORMAT.parse(
        Xsoup.select(
            document,
            "/html/body/div[3]/section/div/div[1]/aside/form/div[2]/span[1]"
        ).get()
    )

    fun getManagedBooks(): ManagedBooks {
        val document = this.client.service.getUserManagedBooks(this.uid).execute().body()!!
        val rawList =
            Xsoup.select(document, "/html/body/div[3]/section/div/div[2]/div[3]/ul/li/a").list()
        var pages = 0
        for (text in rawList) {
            if (INT_REGEX.matches(text))
                pages += 1
        }
        return ManagedBooks(this.client, this, pages)
    }

}

class SelfUser(client: EsjzoneClient, document: Document) : User(client, document)

class ManagedBooks(
    private val client: EsjzoneClient,
    private val user: User,
    private val totalPages: Int
) {

    fun getTotalPages(): Int {
        return this.totalPages
    }

    fun getBooks(page: Int): List<Novel> {
        if (page <= 0)
            return listOf()
        if (page > this.totalPages)
            return listOf()
        val novels = mutableListOf<Novel>()
        val document = this.client.service.getUserManagedBooks(this.user.uid).execute().body()!!
        val names =
            Xsoup.select(
                document,
                "/html/body/div[3]/section/div/div[2]/div[2]/table/tbody/tr/td/div/div/h5/a/text()"
            )
                .list()
        val links =
            Xsoup.select(
                document,
                "/html/body/div[3]/section/div/div[2]/div[2]/table/tbody/tr/td/div/div/h5/a/@href"
            )
                .list()
        for (i in 0 until names.size) {
            val link = links[i]
            novels.add(Novel(this.client, names[i], link.substring(8, link.length - 5)))
        }
        return novels.toImmutableList()
    }

}