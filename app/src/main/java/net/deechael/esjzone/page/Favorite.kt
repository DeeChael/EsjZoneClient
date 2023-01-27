package net.deechael.esjzone.page

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.item.Novel
import net.deechael.esjzone.thread
import net.deechael.esjzone.ui.theme.ESJZoneTheme

@Preview
@Composable
fun FavoritesPreview() {
    Favorites(
        null, pages = 2, currentPage = 1, novels = listOf(
            Novel("测试1", ""),
            Novel("测试2", ""),
            Novel("测试3", ""),
            Novel("测试4", "")
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(context: Context?, pages: Int, currentPage: Int, novels: List<Novel>) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(
                text = "我的收藏",
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            for (novel in novels) {
                Card(
                    onClick = {
                        (context as MainActivity).setContent {
                            ESJZoneTheme {
                                Surface(modifier = Modifier.fillMaxSize()) {
                                    Loading()
                                }
                            }
                        }
                        GlobalScope.launch {
                            thread {
                                val detailedNovel = context.esjzone.getNovelDetail(novel)
                                context.setContent {
                                    ESJZoneTheme {
                                        Surface(modifier = Modifier.fillMaxSize()) {
                                            NovelDetail(context, detailedNovel)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    Text(
                        text = novel.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (currentPage <= 1)
                            return@Button
                        (context as MainActivity).setContent {
                            ESJZoneTheme {
                                Surface(modifier = Modifier.fillMaxSize()) {
                                    Loading()
                                }
                            }
                        }
                        GlobalScope.launch {
                            thread {
                                val nextNovels =
                                    context.esjzone.getFavorite(currentPage - 1)
                                context.setContent {
                                    ESJZoneTheme {
                                        Surface(modifier = Modifier.fillMaxSize()) {
                                            Favorites(
                                                context,
                                                pages = pages,
                                                currentPage = currentPage - 1,
                                                novels = nextNovels
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "上一页")
                }
                Text(
                    text = "第 $currentPage 页",
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                )
                Button(
                    onClick = {
                        if (currentPage >= pages)
                            return@Button
                        (context as MainActivity).setContent {
                            ESJZoneTheme {
                                Surface(modifier = Modifier.fillMaxSize()) {
                                    Loading()
                                }
                            }
                        }
                        GlobalScope.launch {
                            thread {
                                val nextNovels =
                                    context.esjzone.getFavorite(currentPage + 1)
                                context.setContent {
                                    ESJZoneTheme {
                                        Surface(modifier = Modifier.fillMaxSize()) {
                                            Favorites(
                                                context,
                                                pages = pages,
                                                currentPage = currentPage + 1,
                                                novels = nextNovels
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "下一页")
                }
            }
        }
    }
}