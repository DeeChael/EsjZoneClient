package net.deechael.esjzone.client.retrofit

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.http.*

interface EsjzoneService {

    @GET("my/profile")
    fun getUserProfile(@Query("uid") uid: Int): Call<Document>

    @GET("my/book")
    fun getUserManagedBooks(@Query("uid") uid: Int): Call<Document>

    @GET("my/post")
    fun getUserPosts(@Query("uid") uid: Int): Call<Document>

    @GET("my/favorite")
    fun getUserFavorites(@Query("uid") uid: Int): Call<Document>

    @GET("my/profile")
    fun getMyProfile(): Call<Document>

    @GET("my/book")
    fun getMyManagedBooks(): Call<Document>

    @GET("my/post")
    fun getMyPosts(): Call<Document>

    @GET("my/favorite")
    fun getMyFavorites(): Call<Document>

    @GET("my/reply")
    fun getMyReplies(): Call<Document>

    @GET("my/message")
    fun getMyMessages(): Call<Document>

    @GET("my/view")
    fun getMyViewedHistories(): Call<Document>

    @GET("my/record")
    fun getMyExperienceRecords(): Call<Document>

    @GET("my/fixed")
    fun getMyIssues(): Call<Document>

    @GET("my/ticket")
    fun getMyTickets(): Call<Document>

    @GET("my/sys")
    fun getMySystemMessages(): Call<Document>

    @GET("detail/{bookId}.html")
    fun getNovelDetail(@Path("bookId", encoded = true) bookId: String): Call<Document>

    @GET("forum/{bookId}/{chapterId}.html")
    fun getChapterDetail(
        @Path("bookId", encoded = true) bookId: String,
        @Path("chapterId", encoded = true) chapterId: String
    ): Call<Document>

    @GET("forum")
    fun getCategories(): Call<Document>

    @GET("forum/{categoryId}/")
    fun getCategoryNovels(@Path("categoryId", encoded = true) categoryId: String): Call<Document>

    @GET("tags{typeId}{sortId}/")
    fun getNovelsByTag(
        @Path("typeId", encoded = true) typeId: Int,
        @Path("sortId", encoded = true) sortId: Int
    ): Call<Document>

    @GET("tags{typeId}{sortId}/{tag}/")
    fun getNovelsByTag(
        @Path("typeId", encoded = true) typeId: Int,
        @Path("sortId", encoded = true) sortId: Int,
        @Path("tag", encoded = true) tag: String
    ): Call<Document>

    @FormUrlEncoded
    @POST("inc/forum_reply.php")
    // fun createComment(@Part("content") content: String, @Part("data") data: String = "books")
    fun createComment(
        @Header("authorization") authToken: String,
        @Field("content", encoded = true) content: String,
        @Field("data", encoded = true) data: String = "books"
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("inc/forum_reply.php")
    // FIXME not works
    fun createChapterComment(
        @Header("authorization") authToken: String,
        @Field("content", encoded = true) content: String,
        @Field("forumId", encoded = true) chapterId: String,
        @Field("data", encoded = true) data: String = "forum"
    ): Call<ResponseBody>
    // fun createComment(@Body body: RequestBody, @Header("authorization") authToken: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("inc/forum_reply.php")
    // FIXME not works
    fun replyComment(
        @Header("authorization") authToken: String,
        @Field("content", encoded = true) content: String,
        @Field("reply", encoded = true) reply: String,
        @Field("forum_id", encoded = true) forumId: String = "0",
        @Field("data", encoded = true) data: String = "books"
    ): Call<ResponseBody>
    // fun replyComment(@Body body: RequestBody, @Header("authorization") authToken: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("inc/forum_del.php")
    // FIXME not works
    fun deleteComment(
        @Header("authorization") authToken: String,
        @Field("rid", encoded = true) commentId: String,
        @Field("type", encoded = true) type: String = "book",
        @Field("data", encoded = true) data: String = "books"
    ): Call<ResponseBody>
    // fun deleteComment(@Body body: RequestBody, @Header("authorization") authToken: String): Call<ResponseBody>

    @POST("inc/mem_favorite.php")
    fun changeFavoriteStatus(@Header("authorization") authToken: String): Call<ResponseBody> // FIXME not works

    @POST("{path}")
    // fun getAuthToken(@Path("path") path: String, @Part("plxf") plxf: String = "getAuthToken") // Invoke this before every POST request
    fun getAuthToken(
        @Path("path", encoded = true) path: String,
        @Body body: RequestBody
    ): Call<Document> // Invoke this before every POST request

}