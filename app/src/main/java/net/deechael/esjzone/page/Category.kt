package net.deechael.esjzone.page

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.item.Category
import net.deechael.esjzone.thread

@Preview
@Composable
fun CategoriesPreview() {
    Categories(
        null,
        listOf(
            Category("测试1", ""),
            Category("测试2", ""),
            Category("测试3", ""),
            Category("测试4", ""),
            Category("测试5", ""),
            Category("测试6", ""),
            Category("测试7", ""),
            Category("测试8", ""),
            Category("测试9", ""),
            Category("测试10", ""),
            Category("测试11", ""),
            Category("测试12", ""),
            Category("测试13", ""),
            Category("测试14", ""),
            Category("测试15", "")
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(context: Context?, categories: List<Category>) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Text(
                text = "分组列表",
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            for (category in categories) {
                Card(
                    onClick = {
                        (context as EsjZoneActivity).updateContent({
                            Loading()
                        })
                        GlobalScope.launch {
                            thread {
                                val novels = context.esjzone.getNovels(category)
                                context.updateContent({
                                    Novels(
                                        context = context,
                                        categoryName = category.name,
                                        novels = novels
                                    )
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
                        text = category.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}