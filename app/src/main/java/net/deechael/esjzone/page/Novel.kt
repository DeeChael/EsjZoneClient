package net.deechael.esjzone.page

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.item.DetailedNovel
import net.deechael.esjzone.item.Novel
import net.deechael.esjzone.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Novels(context: Context?, categoryName: String, novels: List<Novel>, back: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Text(
                text = categoryName,
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            for (novel in novels) {
                Card(
                    onClick = {
                        (context as EsjZoneActivity).updateContent({
                            Loading()
                        })
                        GlobalScope.launch {
                            thread {
                                val detailedNovel = context.esjzone.getNovelDetail(novel)
                                context.updateContent({
                                    NovelDetail(context, detailedNovel) {
                                        context.updateContent(
                                            {
                                                Novels(
                                                    context = context,
                                                    categoryName = categoryName,
                                                    novels = novels
                                                )
                                            }
                                        )
                                    }
                                })
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = novel.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
    BackHandler(enabled = true, back)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelDetail(context: Context, novel: DetailedNovel, back: () -> Unit = {}) {
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(novel.cover)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier.align(Alignment.TopCenter),
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = null
                )
            }
            Text(
                text = novel.name,
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            for (chapter in novel.chapters) {
                Card(
                    onClick = {
                        (context as EsjZoneActivity).updateContent({
                            Loading()
                        })
                        GlobalScope.launch {
                            thread {
                                val detailedChapter =
                                    context.esjzone.getChapterDetail(chapter)
                                context.updateContent({
                                    Chapter(detailedChapter = detailedChapter)
                                })
                            }
                        }
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
    BackHandler(enabled = true, back)
}