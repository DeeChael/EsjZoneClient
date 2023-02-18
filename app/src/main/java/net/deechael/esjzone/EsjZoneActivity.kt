@file:OptIn(ExperimentalMaterial3Api::class)

package net.deechael.esjzone

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.deechael.esjzone.database.Settings
import net.deechael.esjzone.network.EsjZoneNetwork
import net.deechael.esjzone.page.Loading
import net.deechael.esjzone.page.Login
import net.deechael.esjzone.page.MainPage
import net.deechael.esjzone.page.NetworkInNeed
import net.deechael.esjzone.themes.LatteTheme
import net.deechael.esjzone.themes.MochaTheme
import net.deechael.esjzone.ui.theme.ESJZoneTheme


class EsjZoneActivity : ComponentActivity() {

    lateinit var config: Settings
        private set

    lateinit var esjzone: EsjZoneNetwork
        private set

    lateinit var username: String

    lateinit var avatar: String

    private var defaultTheme: @Composable (Boolean, Boolean, ctx: @Composable () -> Unit) -> Unit =
        { a, b, c ->
            ESJZoneTheme(a, b, c)
        }

    var previousContent: @Composable () -> Unit = {
        Loading()
    }
        private set

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = Settings(this)
        esjzone = EsjZoneNetwork(this)

        val general = config.getGeneral()
        this.updateTheme(general.theme)
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
            esjzone.updateCookie(cache.wsKey, cache.wsToken)
            GlobalScope.launch {
                Looper.prepare()
                toast("正在获取信息")
                thread {
                    if (esjzone.checkLoggedIn()) {
                        this@EsjZoneActivity.username = esjzone.getUsername()
                        // this@MainActivity.avatar = esjzone.getAvatar()
                        val categories = this@EsjZoneActivity.esjzone.getCategories()
                        updateContent({
                            MainPage(context = this@EsjZoneActivity, categories)
                        })
                    } else {
                        toast("您没有登录")
                        updateContent({
                            Login(this@EsjZoneActivity)
                        })
                    }
                }
            }
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
        val general = this.config.getGeneral()
        general.theme = index
        this.config.saveGeneral(general)
    }

    private fun toast(text: String) {
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