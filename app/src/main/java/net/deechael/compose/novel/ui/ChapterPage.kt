package net.deechael.compose.novel.ui

import android.annotation.SuppressLint
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import net.deechael.compose.novel.item.ChapterBreakLine
import net.deechael.compose.novel.item.ChapterContent
import net.deechael.compose.novel.item.ChapterImage
import net.deechael.compose.novel.item.ChapterText

@Preview
@Composable
fun ChapterPreview() {
    ChapterPage(
        title = "测试章节",
        chapterContents = listOf(
            ChapterText("这是第一段"),
            ChapterText("这是第二段"),
            ChapterBreakLine(),
            ChapterText("这是第三段"),
            ChapterText("这是第四段"),
            ChapterImage("https://img.kookapp.cn/assets/2023-01/rhv1ugUjQw0dd0ef.png"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的")
        )
    )
}

@Preview
@Composable
fun ChapterWithToolbarPreview() {
    ChapterPage(
        title = "测试章节",
        chapterContents = listOf(
            ChapterText("这是第一段"),
            ChapterText("这是第二段"),
            ChapterBreakLine(),
            ChapterText("这是第三段"),
            ChapterText("这是第四段"),
            ChapterImage("https://img.kookapp.cn/assets/2023-01/rhv1ugUjQw0dd0ef.png"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的"),
            ChapterText("这是第五段按时到海南岛啊后的念诵偶的号i是你都i的那艘i年第哦啊三农i阿森纳都i送i那点滴哦三栋爱上你的拿爱死你都i三年底哦那四年第三地哦那电脑i你都i阿森纳的内塞哦你定阿松i你都i阿斯年底难Dion扫i你都爱上你都i阿森纳i哦那嗲是农地那艘i的那四年的")
        ),
        showToolbar = true
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterPage(
    title: String,
    chapterContents: List<ChapterContent>,
    showToolbar: Boolean = false
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

    var toolbarShowed by rememberSaveable { mutableStateOf(showToolbar) }

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
                        text = title,
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
                Text(
                    text = title,
                    fontSize = 6.em,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                for (content in chapterContents) {
                    content.toComposable()()
                }
            }
        }
    }
}