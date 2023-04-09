package net.deechael.esjzone.client.retrofit

import okhttp3.FormBody
import okhttp3.RequestBody

class BodyBuilder private constructor() {

    private val params: MutableMap<String, String> = mutableMapOf()

    fun param(key: String, value: String): BodyBuilder {
        this.params[key] = value
        return this
    }

    fun build(): RequestBody {
        return FormBody.Builder()
            .apply {
                for (key in params.keys)
                    this.addEncoded(key, params[key]!!)
            }
            .build()
    }

    companion object {

        fun of(): BodyBuilder {
            return BodyBuilder()
        }

    }

}