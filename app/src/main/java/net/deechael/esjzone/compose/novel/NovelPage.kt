package net.deechael.esjzone.compose.novel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.client.chapter.Chapter
import net.deechael.esjzone.client.novel.Novel
import net.deechael.esjzone.compose.general.Indicator

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NovelPage(context: Context?, novel: Novel, navController: NavController) {
    var chapters = listOf<Chapter>()
    context as EsjZoneActivity
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        GlobalScope.launch {
            novel.reload()
            chapters = novel.listChapters()
        }
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    refreshScope.launch {
        refreshing = true
        GlobalScope.launch {
            novel.reload()
            chapters = novel.listChapters()
        }
        refreshing = false
    }

    Box(
        Modifier.pullRefresh(state)
    ) {
        Column {
            if (!refreshing) {
                Row {
                    Box {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(novel.cover)
                                .crossfade(true)
                                .build(),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                                .requiredWidthIn(max = (LocalConfiguration.current.screenWidthDp / 2).dp),
                            loading = {
                                Indicator()
                            },
                            contentDescription = null
                        )
                    }
                    Box {
                        Column {
                            Box {
                                Text(
                                    text = novel.name,
                                    fontSize = 5.em,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.TopCenter)
                                )
                            }
                            Button(
                                onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(
                                    text = "评论区",
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
                Divider()
                for (chapter in chapters) {
                    Card(
                        onClick = {
                            EsjZoneActivity.viewing.chapter = chapter
                            navController.navigate(route = "chapter")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = chapter.name,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(refreshing, state, modifier = Modifier.align(Alignment.TopCenter))
    }
}