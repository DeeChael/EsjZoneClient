package net.deechael.esjzone

import net.deechael.esjzone.network.EsjZoneNetwork
import org.jsoup.Jsoup
import org.junit.Test
import java.io.FileOutputStream
import java.io.FileWriter
import java.net.InetSocketAddress
import java.net.Proxy
import java.nio.charset.StandardCharsets

class EsjZoneTest {

    @Test
    fun mainPage() {
        val network = EsjZoneNetwork(null)
        val mainPage = network.getMainPage().bytes()
        val outputStream = FileOutputStream("test.html")
        outputStream.write(mainPage)
        outputStream.close()
    }

}