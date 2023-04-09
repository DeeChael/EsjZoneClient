package net.deechael.esjzone.compose.general

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.client.category.Category

var categories = listOf<Category>()

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun Home(context: Context?, navController: NavController) {
    context as EsjZoneActivity
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        GlobalScope.launch {
            categories = context.esjzone.listCategories()
        }
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    refreshScope.launch {
        refreshing = true
        GlobalScope.launch {
            categories = context.esjzone.listCategories()
        }
        refreshing = false
    }

    Box(
        Modifier.pullRefresh(state)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "分组列表",
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false)
            ) {
                if (!refreshing) {
                    for (category in categories) {
                        Card(
                            onClick = {
                                EsjZoneActivity.viewing.category = category
                                navController.navigate(route = "category")
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
        PullRefreshIndicator(refreshing, state, modifier = Modifier.align(Alignment.TopCenter))
    }

}