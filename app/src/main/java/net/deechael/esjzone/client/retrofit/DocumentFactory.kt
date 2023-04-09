package net.deechael.esjzone.client.retrofit

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class DocumentFactory : Converter.Factory(), Converter<ResponseBody, Document> {

    override fun convert(responseBody: ResponseBody): Document {
        return Jsoup.parse(responseBody.string())
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Document> {
        return this
    }

}