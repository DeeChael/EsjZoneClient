package net.deechael.esjzone.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import net.deechael.esjzone.item.DetailedChapter
import net.deechael.esjzone.item.content.ChapterText
import net.deechael.esjzone.item.content.ChatperBreakLine
import net.deechael.esjzone.item.content.ChatperImage

@Preview
@Composable
fun ChapterPreview() {
    Chapter(
        detailedChapter = DetailedChapter(
            "测试章节",
            chapterContents = listOf(
                ChapterText("这是第一段"),
                ChapterText("这是第二段"),
                ChatperBreakLine(),
                ChapterText("这是第三段"),
                ChapterText("这是第四段"),
                ChatperImage("https://img.kookapp.cn/assets/2023-01/rhv1ugUjQw0dd0ef.png"),
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
    )
}

@Composable
fun Chapter(detailedChapter: DetailedChapter) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Text(
                text = detailedChapter.title,
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            for (content in detailedChapter.chapterContents) {
                if (content is ChapterText) {
                    Text(
                        text = content.content,
                        modifier = Modifier.padding(4.dp)
                    )
                } else if (content is ChatperImage) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(content.url)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp)
                    )
                } else if (content is ChatperBreakLine) {
                    Text(
                        text = "",
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        }
    }
}
