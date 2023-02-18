package net.deechael.esjzone.network

import android.content.Context
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.config.CacheConfig
import net.deechael.esjzone.item.Category
import net.deechael.esjzone.item.Chapter
import net.deechael.esjzone.item.DetailedChapter
import net.deechael.esjzone.item.DetailedNovel
import net.deechael.esjzone.item.Novel
import net.deechael.esjzone.item.content.ChapterBreakLine
import net.deechael.esjzone.item.content.ChapterContent
import net.deechael.esjzone.item.content.ChapterImage
import net.deechael.esjzone.item.content.ChapterText
import okhttp3.Callback
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.toImmutableList
import org.jsoup.Jsoup
import us.codecraft.xsoup.Xsoup
import java.net.Proxy

class EsjZoneNetwork(context: Context?, proxy: Proxy? = null) {

    companion object {

        const val USERNAME_XPATH =
            "/html/body/div[3]/section/div/div[1]/aside/form/div[2]/h4/text()"
        const val AVATAR_XPATH = "/html/body/div[3]/section/div/div[1]/aside/form/div[1]/img/@src"

        private const val CATEGORY_NAME =
            "/html/body/div[3]/section/div/div[1]/div[1]/table/tbody/tr/td/a"

        private const val NOVEL_TITLE = "/html/body/div[3]/section/div/div/div/table/tbody/tr/td/a"
        private const val NOVEL_COVER =
            "/html/body/div[3]/section/div/div[1]/div[1]/div[1]/div[1]/a/img/@src"
        private const val NOVEL_CHAPTER_NAMES =
            "/html/body/div[3]/section/div/div[1]/div[5]/div/div/div/div[2]/a/p/text()"
        private const val NOVEL_CHAPTER_URLS =
            "/html/body/div[3]/section/div/div[1]/div[5]/div/div/div/div[2]/a/@href"

        private const val CHAPTER_TITLE = "/html/body/div[3]/section/div/div[1]/h2/text()"
        private const val CHAPTER_CONTENT = "/html/body/div[3]/section/div/div[1]/div[3]"
    }

    private val context: Context?
    private var proxy: Proxy?

    private var httpClient: OkHttpClient

    init {
        this.context = context
        this.proxy = proxy
        val builder = OkHttpClient.Builder()
        if (this.proxy != null)
            builder.proxy(this.proxy)
        this.httpClient = builder.build()
    }

    fun updateCookie(wsKey: String, wsToken: String) {
        val builder = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf(
                        Cookie.Builder().domain("www.esjzone.cc").name("ws_key").value(wsKey)
                            .build(),
                        Cookie.Builder().domain("www.esjzone.cc").name("ws_token").value(wsToken)
                            .build()
                    )
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                }
            })
        if (this.proxy != null)
            builder.proxy(this.proxy)
        this.httpClient = builder.build()
        val cache = CacheConfig()
        cache.wsKey = wsKey
        cache.wsToken = wsToken
        (context as EsjZoneActivity).config.saveCache(cache)
    }

    fun getMainPage(): ResponseBody {
        val request = builder()
            .url("https://www.esjzone.cc")
            .get()
            .build()
        val call = httpClient.newCall(request)
        val response = call.execute()
        return response.body!!
    }

    fun getUsername(): String {
        val request = builder()
            .url("https://www.esjzone.cc/my/profile")
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val results = Xsoup.compile(USERNAME_XPATH).evaluate(document).list()
        if (results.size <= 0)
            throw RuntimeException("Not logged in")
        return results[0]
    }

    fun getAvatar(): String {
        val request = builder()
            .url("https://www.esjzone.cc/my/profile")
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val results = Xsoup.compile(AVATAR_XPATH).evaluate(document).list()
        if (results.size <= 0)
            throw RuntimeException("Not logged in")
        return "https://www.esjzone.cc" + results[0]
    }

    fun checkLoggedIn(): Boolean {
        return try {
            val request = builder()
                .url("https://www.esjzone.cc/my/profile")
                .get()
            val document = Jsoup.parse(body(request.build()).string())
            Xsoup.compile(USERNAME_XPATH).evaluate(document).list().size > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getFavorite(page: Int): List<Novel> {
        if (page < 1)
            return listOf()
        if (page > getFavoritePages())
            return listOf()
        val request = builder()
            .url("https://www.esjzone.cc/my/favorite/$page")
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val names =
            Xsoup.compile("/html/body/div[3]/section/div/div[2]/div[3]/table/tbody/tr/td/div/div/h5/a/text()")
                .evaluate(document).list()
        val urls =
            Xsoup.compile("/html/body/div[3]/section/div/div[2]/div[3]/table/tbody/tr/td/div/div/h5/a/@href")
                .evaluate(document).list()

        val novels = mutableListOf<Novel>()
        for (i in 0 until names.size) {
            novels.add(Novel(names[i], "https://www.esjzone.cc" + urls[i]))
        }
        return novels.toImmutableList()
    }

    fun getFavoritePages(): Int {
        val request = builder()
            .url("https://www.esjzone.cc/my/favorite")
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val raw = Xsoup.compile("/html/body/div[3]/section/div/div[2]/div[4]/ul/li[5]/a/@href")
            .evaluate(document).list()[0]
        return raw.substring(13).toIntOrNull() ?: 1
    }

    fun futureCheckLoggedIn(callback: Callback) {
        val request = builder()
            .url("https://www.esjzone.cc/my/profile")
            .get()
        val call = httpClient.newCall(request.build())
        call.enqueue(callback)
    }

    fun login(email: String, password: String): Boolean {
        try {
            val loginRequest = builder().url("https://www.esjzone.cc/my/login")
                .post(
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("plxf", "getAuthToken")
                        .build()
                ).build()
            httpClient.newCall(loginRequest).execute()
            var wsKey: String = ""
            var wsToken: String = ""
            val builder = OkHttpClient.Builder()
                .cookieJar(object : CookieJar {
                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        return listOf()
                    }

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        for (cookie in cookies) {
                            if (cookie.name == "ws_key") {
                                wsKey = cookie.value
                            } else if (cookie.name == "ws_token") {
                                wsToken = cookie.value
                            }
                        }
                    }
                })
            if (this.proxy != null)
                builder.proxy(this.proxy)
            this.httpClient = builder.build()
            val memLoginRequest = builder().url("https://www.esjzone.cc/inc/mem_login.php")
                .post(
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("email", email)
                        .addFormDataPart("pwd", password)
                        .addFormDataPart("remember_me", "on")
                        .build()
                ).build()
            this.httpClient.newCall(memLoginRequest).execute()
            updateCookie(wsKey, wsToken)
            return true
        } catch (e: Exception) {
            return checkLoggedIn()
        }
    }

    fun getCategories(): List<Category> {
        val request = builder()
            .url("https://www.esjzone.cc/forum/")
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val names = Xsoup.compile("$CATEGORY_NAME/text()").evaluate(document).list()
        val urls = Xsoup.compile("$CATEGORY_NAME/@href").evaluate(document).list()
        val categories = mutableListOf<Category>()
        for (i in 0 until names.size) {
            categories.add(Category(names[i], "https://www.esjzone.cc" + urls[i]))
        }
        return categories.toImmutableList()
    }

    fun getNovels(category: Category): List<Novel> {
        val request = builder()
            .url(category.url)
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val names = Xsoup.compile("$NOVEL_TITLE/text()").evaluate(document).list()
        val urls = Xsoup.compile("$NOVEL_TITLE/@href").evaluate(document).list()
        val novels = mutableListOf<Novel>()
        for (i in 0 until names.size) {
            var url = urls[i]
            url = url.substring(1, url.length - 1)
            novels.add(
                Novel(
                    names[i],
                    "https://www.esjzone.cc/detail/" + url.split("/")[2] + ".html"
                )
            )
        }
        return novels.toImmutableList()
    }

    fun getNovelDetail(novel: Novel): DetailedNovel {
        val request = builder()
            .url(novel.url)
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val cover = Xsoup.compile(NOVEL_COVER).evaluate(document).list()
        val names = Xsoup.compile(NOVEL_CHAPTER_NAMES).evaluate(document).list()
        val urls = Xsoup.compile(NOVEL_CHAPTER_URLS).evaluate(document).list()
        val chapters = mutableListOf<Chapter>()
        for (i in 0 until names.size) {
            chapters.add(Chapter(names[i], urls[i]))
        }
        return DetailedNovel(novel.name, if (cover.size > 0) cover[0] else "", novel.url, chapters)
    }

    fun getChapterDetail(chapter: Chapter): DetailedChapter {
        val request = builder()
            .url(chapter.url)
            .get()
        val document = Jsoup.parse(body(request.build()).string())
        val title = Xsoup.compile(CHAPTER_TITLE).evaluate(document).list()[0]
        val chapterContents = mutableListOf<ChapterContent>()
        for (rawContent in Xsoup.compile(CHAPTER_CONTENT)
            .evaluate(document).elements[0].allElements) {
            if (rawContent.getElementsByTag("br").size > 0) {
                chapterContents.add(ChapterBreakLine())
            } else if (rawContent.getElementsByTag("img").size > 0) {
                chapterContents.add(ChapterImage(rawContent.getElementsByTag("img")[0].attr("src")))
            } else {
                chapterContents.add(ChapterText(rawContent.text()))
            }
        }
        return DetailedChapter(title, chapterContents)
    }

    private fun response(request: Request): Response {
        val call = httpClient.newCall(request)
        return call.execute()
    }

    private fun body(request: Request): ResponseBody {
        val call = httpClient.newCall(request)
        val response = call.execute()
        return response.body!!
    }

    private fun builder(): Request.Builder {
        return Request.Builder()
            .header(
                "accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
            )
            .header("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh-Hans;q=0.7,zh;q=0.6")
            .header(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
            )
    }

}