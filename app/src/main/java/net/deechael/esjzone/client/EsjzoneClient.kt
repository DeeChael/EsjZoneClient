package net.deechael.esjzone.client

import net.deechael.esjzone.client.category.Category
import net.deechael.esjzone.client.novel.Novel
import net.deechael.esjzone.client.retrofit.BodyBuilder
import net.deechael.esjzone.client.retrofit.DocumentFactory
import net.deechael.esjzone.client.retrofit.EsjzoneService
import net.deechael.esjzone.client.types.Sorts
import net.deechael.esjzone.client.types.Types
import net.deechael.esjzone.client.user.SelfUser
import net.deechael.esjzone.client.user.User
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.toImmutableList
import org.jsoup.Jsoup
import retrofit2.Retrofit
import us.codecraft.xsoup.Xsoup
import java.net.InetSocketAddress
import java.net.Proxy

class EsjzoneClient internal constructor(wsKey: String, wsToken: String, proxy: Proxy? = null) {

    val httpClient: OkHttpClient
    val retrofit: Retrofit
    val service: EsjzoneService

    private var self: SelfUser? = null

    init {
        val builder = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf(
                        Cookie.Builder().domain("www.esjzone.cc").name("ews_key").value(wsKey)
                            .build(),
                        Cookie.Builder().domain("www.esjzone.cc").name("ews_token").value(wsToken)
                            .build()
                    )
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                }
            })
            .addInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .header(
                            "accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
                        )
                        .header(
                            "accept-language",
                            "en-US,en;q=0.9,zh-CN;q=0.8,zh-Hans;q=0.7,zh;q=0.6"
                        )
                        .header(
                            "user-agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
                        )
                        .build()
                )
            }
        if (proxy != null)
            builder.proxy(proxy)
        this.httpClient = builder.build()
        this.retrofit = Retrofit.Builder()
            .baseUrl("https://www.esjzone.cc/")
            .client(this.httpClient)
            .addConverterFactory(DocumentFactory())
            .build()
        this.service = this.retrofit.create(EsjzoneService::class.java)
    }

    fun getAuthToken(url: String): String {
        val response =
            this.service.getAuthToken(url, BodyBuilder.of().param("plxf", "getAuthToken").build())
                .execute()
        val document = response.body()!!
        val text = document.getElementsByTag("JinJing")[0].text()
        return text
    }

    fun getUserInfo(uid: Int): User {
        return User(this, this.service.getUserProfile(uid).execute().body()!!)
    }

    fun getMe(): SelfUser {
        if (this.self == null)
            this.self = SelfUser(this, this.service.getMyProfile().execute().body()!!)
        return this.self!!
    }

    fun listCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val document = this.service.getCategories().execute().body()!!
        for (element in Xsoup.select(
            document,
            "/html/body/div[3]/section/div/div[1]/div[1]/table/tbody/tr/td/a"
        ).elements) {
            val rawUrl = element.attr("href")
            categories.add(Category(this, rawUrl.substring(7, rawUrl.length - 1), element.text()))
        }
        return categories.toImmutableList()
    }

    /**
     * 列出小说
     * @param type 小说类型
     * @param sort 筛选方法
     * @return 获得到的小说列表
     */
    fun listNovels(type: Types = Types.ALL, sort: Sorts = Sorts.RECENTLY_UPDATED): List<Novel> {
        val novels = mutableListOf<Novel>()
        val document = this.service.getNovelsByTag(type.index, sort.index).execute().body()!!
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
            val id = rawUrl.substring(8, rawUrl.length - 5)
            novels.add(
                Novel(
                    client = this,
                    id = id,
                    name = rawInfos[i].text(),
                    cover = if (rawCoverUrl.startsWith("/assets")) "https://www.esjzone.cc$rawCoverUrl" else rawCoverUrl
                )
            )
        }
        return novels.toImmutableList()
    }

    fun isLoggedIn(): Boolean {
        if (this.self != null)
            return true
        val document = this.service.getMyProfile().execute().body()!!
        return Xsoup.compile("/html/body/div[3]/section/div/div[1]/aside/form/div[2]/h4/text()")
            .evaluate(document).list().size > 0
    }

}

class EsjzoneClientProxiedBuilder private constructor(private val proxy: Proxy) {

    fun cookie(wsKey: String, wsToken: String): EsjzoneClient {
        return EsjzoneClientBuilder.of().proxy(this.proxy).key(wsKey).token(wsToken).build()
    }

    fun login(email: String, password: String): EsjzoneClient {
        return EsjzoneLoginer.of().proxy(this.proxy).login(email, password)
    }

    companion object {

        fun of(proxy: Proxy): EsjzoneClientProxiedBuilder {
            return EsjzoneClientProxiedBuilder(proxy)
        }

        fun of(type: Proxy.Type, host: String, port: Int): EsjzoneClientProxiedBuilder {
            return of(Proxy(type, InetSocketAddress(host, port)))
        }

    }

}

class EsjzoneClientBuilder private constructor() {

    private var proxy: Proxy? = null
    private lateinit var wsKey: String
    private lateinit var wsToken: String

    fun proxy(proxy: Proxy?): EsjzoneClientBuilder {
        this.proxy = proxy
        return this
    }

    fun key(wsKey: String): EsjzoneClientBuilder {
        this.wsKey = wsKey
        return this
    }

    fun token(wsToken: String): EsjzoneClientBuilder {
        this.wsToken = wsToken
        return this
    }

    fun build(): EsjzoneClient {
        return EsjzoneClient(this.wsKey, this.wsToken, this.proxy)
    }

    companion object {

        fun of(): EsjzoneClientBuilder {
            return EsjzoneClientBuilder()
        }

    }

}

class EsjzoneLoginer private constructor() {


    private var proxy: Proxy? = null

    fun proxy(proxy: Proxy?): EsjzoneLoginer {
        this.proxy = proxy
        return this
    }

    fun login(email: String, password: String): EsjzoneClient {
        var builder = OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .header(
                            "accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
                        )
                        .header(
                            "accept-language",
                            "en-US,en;q=0.9,zh-CN;q=0.8,zh-Hans;q=0.7,zh;q=0.6"
                        )
                        .header(
                            "user-agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
                        )
                        .build()
                )
            }
        if (this.proxy != null)
            builder.proxy(this.proxy)
        var httpClient = builder.build()
        val loginRequest = Request.Builder().url("https://www.esjzone.cc/my/login")
            .post(
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("plxf", "getAuthToken")
                    .build()
            ).build()
        val response = httpClient.newCall(loginRequest).execute()
        val authorization =
            Jsoup.parse(response.body!!.string()).getElementsByTag("JinJing")[0].text()
        response.close()
        var wsKey: String = ""
        var wsToken: String = ""
        builder = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    for (cookie in cookies) {
                        if (cookie.name == "ews_key") {
                            wsKey = cookie.value
                        } else if (cookie.name == "ews_token") {
                            wsToken = cookie.value
                        }
                    }
                }
            })
            .addInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .header(
                            "accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
                        )
                        .header(
                            "accept-language",
                            "en-US,en;q=0.9,zh-CN;q=0.8,zh-Hans;q=0.7,zh;q=0.6"
                        )
                        .header(
                            "user-agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
                        )
                        .build()
                )
            }
        if (this.proxy != null)
            builder.proxy(this.proxy)
        httpClient = builder.build()
        val memLoginRequest = Request.Builder().url("https://www.esjzone.cc/inc/mem_login.php")
            .post(
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("pwd", password)
                    .addFormDataPart("remember_me", "on")
                    .build()
            )
            .header("authorization", authorization)
            .header(
                "accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
            )
            .header("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh-Hans;q=0.7,zh;q=0.6")
            .header(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
            )
            .build()
        val resp = httpClient.newCall(memLoginRequest).execute()
        resp.close()
        return EsjzoneClientBuilder.of()
            .key(wsKey)
            .token(wsToken)
            .proxy(this.proxy)
            .build()
    }

    companion object {

        fun of(): EsjzoneLoginer {
            return EsjzoneLoginer()
        }

    }

}