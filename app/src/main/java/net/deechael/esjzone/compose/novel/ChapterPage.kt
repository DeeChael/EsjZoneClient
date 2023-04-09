package net.deechael.esjzone.compose.novel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kotlinx.coroutines.launch
import net.deechael.esjzone.client.chapter.Chapter
import net.deechael.esjzone.client.chapter.ChapterContent
import net.deechael.esjzone.compose.general.Indicator

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterPage(
    context: Context?,
    chapter: Chapter
) {
    val gap =
        with(LocalDensity.current) { (LocalConfiguration.current.screenWidthDp / 3).dp.toPx() }
    val screenWidth =
        with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeight =
        with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val left = gap
    val right = screenWidth - gap
    val top = gap
    val bottom = screenHeight - gap

    var toolbarShowed by rememberSaveable { mutableStateOf(false) }

    val loadingScope = rememberCoroutineScope()
    var loading by rememberSaveable { mutableStateOf(true) }

    val contents = mutableListOf<ChapterContent>()

    loadingScope.launch {
        contents.addAll(chapter.contents!!)
        loading = false
    }

    Scaffold(
        modifier = Modifier
            .clickable {}
            .pointerInput(Unit) {
                detectTapGestures {
                    if (it.x < left)
                        return@detectTapGestures
                    if (it.x > right)
                        return@detectTapGestures
                    if (it.y < top)
                        return@detectTapGestures
                    if (it.y > bottom)
                        return@detectTapGestures
                    toolbarShowed = !toolbarShowed
                }
            },
        topBar = {
            if (toolbarShowed) {
                NavigationBar {
                    IconButton(
                        onClick = {
                            // TODO
                        },
                        modifier = Modifier.padding(
                            top = 26.dp,
                            start = 8.dp,
                            bottom = 26.dp,
                            end = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "返回"
                        )
                    }
                    Text(
                        text = chapter.name,
                        modifier = Modifier.padding(18.dp),
                        fontSize = 6.em
                    )
                }
            }
        },
        bottomBar = {
            if (toolbarShowed) {
                NavigationBar {
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            // TODO
                        }, label = {
                            Text(
                                text = "设置",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 4.em
                            )
                        }, icon = {})
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            // TODO
                        }, label = {
                            Text(
                                text = "目录",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 4.em
                            )
                        }, icon = {})
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            // TODO
                        }, label = {
                            Text(
                                text = "评论",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 4.em
                            )
                        }, icon = {})
                }
            }
        },
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false)
            ) {
                if (loading) {
                    Indicator()
                } else {
                    Text(
                        text = chapter.name,
                        fontSize = 6.em,
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider()
                    for (content in contents) {
                        content.toComposable()()
                    }
                }
            }
        }
    }
}