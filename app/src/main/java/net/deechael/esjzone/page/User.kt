package net.deechael.esjzone.page

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.thread

@Preview
@Composable
fun UserPreview() {
    User(null, username = "DeeChael")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun User(context: Context?, username: String) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = username,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                fontSize = 6.em
            )
        }
        Divider()
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Card(
                onClick = {
                    (context as EsjZoneActivity).updateContent({
                        Loading()
                    })
                    GlobalScope.launch {
                        thread {
                            val pages = context.esjzone.getFavoritePages()
                            val novels = context.esjzone.getFavorite(1)
                            context.updateContent({
                                Favorites(
                                    context,
                                    pages = pages,
                                    currentPage = 1,
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
                Row {
                    Text(
                        text = "????????????",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}