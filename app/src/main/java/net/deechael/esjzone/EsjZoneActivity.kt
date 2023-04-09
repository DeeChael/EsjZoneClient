@file:OptIn(ExperimentalMaterial3Api::class)

package net.deechael.esjzone

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.withContext
import net.deechael.esjzone.client.EsjzoneClient
import net.deechael.esjzone.client.EsjzoneClientBuilder
import net.deechael.esjzone.client.category.Category
import net.deechael.esjzone.client.chapter.Chapter
import net.deechael.esjzone.client.novel.Novel
import net.deechael.esjzone.compose.Start
import net.deechael.esjzone.compose.general.Loading
import net.deechael.esjzone.compose.general.NetworkInNeed
import net.deechael.esjzone.database.Settings
import net.deechael.esjzone.themes.LatteTheme
import net.deechael.esjzone.themes.MochaTheme
import net.deechael.esjzone.ui.theme.ESJZoneTheme

class EsjZoneActivity : ComponentActivity() {

    lateinit var config: Settings
        private set

    lateinit var esjzone: EsjzoneClient

    private var defaultTheme: @Composable (Boolean, Boolean, ctx: @Composable () -> Unit) -> Unit =
        { a, b, c ->
            ESJZoneTheme(a, b, c)
        }

    object viewing {

        lateinit var category: Category
        lateinit var novel: Novel
        lateinit var chapter: Chapter

    }

    var previousContent: @Composable () -> Unit = {
        Loading()
    }
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = Settings(this)

        this.updateTheme(2)
        if (!checkNetwork()) {
            connectedBefore = false
            Toast.makeText(this, "无法连接至互联网", Toast.LENGTH_SHORT).show()
            updateContent({
                NetworkInNeed()
            })
        } else {
            updateContent({
                Loading()
            })
            val cache = config.getCache()

            esjzone = EsjzoneClientBuilder.of()
                .key(cache.wsKey)
                .token(cache.wsToken)
                .build()
            updateContent({
                Start(context = this)
            })
        }
    }

    fun updateContentWithoutUpdate(
        content: @Composable () -> Unit,
        theme: @Composable (Boolean, Boolean, ctx: @Composable () -> Unit) -> Unit = defaultTheme
    ) {
        setContent {
            theme(!isSystemInDarkTheme(), true) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    content()
                }
            }
        }
    }

    fun updateContent(
        content: @Composable () -> Unit,
        theme: @Composable (Boolean, Boolean, ctx: @Composable () -> Unit) -> Unit = defaultTheme
    ) {
        this.previousContent = content
        updateContentWithoutUpdate(content, theme)
    }

    fun updateTheme(index: Int) {
        when (index) {
            1 -> {
                this.defaultTheme = { a, b, c ->
                    LatteTheme(a, b, c)
                }
            }

            2 -> {
                this.defaultTheme = { a, b, c ->
                    MochaTheme(a, b, c)
                }
            }

            else -> {
                this.defaultTheme = { a, b, c ->
                    ESJZoneTheme(a, b, c)
                }
            }
        }
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun checkNetwork(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ?: false
    }

    companion object {

        var connectedBefore: Boolean = true

    }

}

suspend fun thread(runnable: Runnable) = withContext(Dispatchers.Default) {
    runnable.run()
}