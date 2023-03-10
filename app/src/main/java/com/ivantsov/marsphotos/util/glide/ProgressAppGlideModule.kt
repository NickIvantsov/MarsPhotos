package com.ivantsov.marsphotos.util.glide

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.*
import okio.*
import java.io.InputStream


@GlideModule
class ProgressAppGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request: Request = chain.request()
                val response: Response = chain.proceed(request)

                val listener: ResponseProgressListener = DispatchingProgressListener()

                println("request.url(): ${request.url()}")
                response.newBuilder().body(
                    OkHttpProgressResponseBody(
                        request.url(),
                        response.body()!!,
                        listener
                    )
                ).build()
            }.build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    private interface ResponseProgressListener {
        fun update(url: HttpUrl, bytesRead: Long, contentLength: Long)
    }

    interface UIonProgressListener {
        fun onProgress(bytesRead: Long, expectedLength: Long)

        /**
         * Control how often the listener needs an update. 0% and 100% will always be dispatched.
         * @return in percentage (0.2 = call [.onProgress] around every 0.2 percent of progress)
         */
        val granualityPercentage: Float
    }

    private class DispatchingProgressListener : ResponseProgressListener {
        private val handler: Handler = Handler(Looper.getMainLooper())

        override fun update(url: HttpUrl, bytesRead: Long, contentLength: Long) {
            println("update(url: $url, bytesRead: $bytesRead, contentLength: $contentLength)")
            System.out.printf("%s: %d/%d = %.2f%%%n", url, bytesRead, contentLength, (100f * bytesRead) / contentLength)
            val key = url.toString()
            println("url key: $key")
            println("url: ${LISTENERS[getNameOfFile(key)]}")
            val listener = LISTENERS[getNameOfFile(key)] ?: return
            if (contentLength <= bytesRead) {
                forget(key)
            }
            val needsDispatch =
                needsDispatch(key, bytesRead, contentLength, listener.granualityPercentage)
            println("needsDispatch: $needsDispatch")
            if (needsDispatch) {
                handler.post { listener.onProgress(bytesRead, contentLength) }
            }
        }

        private fun needsDispatch(
            key: String,
            current: Long,
            total: Long,
            granularity: Float
        ): Boolean {
            if (granularity == 0f || current == 0L || total == current) {
                return true
            }
            val percent = 100f * current / total
            val currentProgress = (percent / granularity).toLong()
            val lastProgress = PROGRESSES[key]
            return if (lastProgress == null || currentProgress != lastProgress) {
                PROGRESSES[key] = currentProgress
                true
            } else {
                false
            }
        }

        companion object {
            private val LISTENERS: MutableMap<String, UIonProgressListener> = HashMap()
            private val PROGRESSES: MutableMap<String, Long> = HashMap()
            fun forget(url: String) {
                LISTENERS.remove(getNameOfFile(url))
                PROGRESSES.remove(url)
            }

            fun registerListener(url: String, listener: UIonProgressListener) {
                println("STEP 4 save registerListener: url -> $url | listener -> $listener")
                LISTENERS[getNameOfFile(url)] = listener
                println("STEP 5 LISTENERS size: ${LISTENERS.size}")
            }
        }

    }

    private class OkHttpProgressResponseBody(
        private val url: HttpUrl,
        private val responseBody: ResponseBody,
        private val progressListener: ResponseProgressListener
    ) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody.contentType()
        }

        override fun contentLength(): Long {
            return responseBody.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody.source()).buffer()
            }
            return bufferedSource as BufferedSource
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    val fullLength = responseBody.contentLength()
                    if (bytesRead == -1L) { // this source is exhausted
                        totalBytesRead = fullLength
                    } else {
                        totalBytesRead += bytesRead
                    }
                    progressListener.update(url, totalBytesRead, fullLength)
                    return bytesRead
                }
            }
        }
    }

    companion object {
        fun forget(url: String) {
            DispatchingProgressListener.forget(url)
        }

        fun registerListener(url: String, listener: UIonProgressListener) {
            println("Step 3: register listener: url: $url, listener: $listener")
            DispatchingProgressListener.registerListener(url, listener)
            println("result: ${getNameOfFile(url)} | url -> $url")
        }

        fun getNameOfFile(url: String): String{
            val regex = """([^\\/]+)$""".toRegex()
            println("pattern ${regex.pattern}")
            val nam = url.split(regex)
            println(nam.joinToString())
            return url.replace(nam.first(),"")
        }
    }
}