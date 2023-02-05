package net.deechael.esjzone.page

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import net.deechael.esjzone.EsjZoneActivity

@Preview
@Composable
fun SettingsPreview() {
    Settings(null)
}

@Preview
@Composable
fun ThemeDialogPreview() {
    ThemeDialog(null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDialog(context: Context?) {
    Dialog(onDismissRequest = {}) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false)
            ) {
                Text(
                    text = "选择主题",
                    fontSize = 6.em,
                    modifier = Modifier.padding(12.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        (context as EsjZoneActivity).updateTheme(0)
                        context.updateContent({
                            Settings(context)
                        })
                    }
                ) {
                    Text(
                        text = "Material 3",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        (context as EsjZoneActivity).updateTheme(1)
                        context.updateContent({
                            Settings(context)
                        })
                    }
                ) {
                    Text(
                        text = "Latte",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        (context as EsjZoneActivity).updateTheme(2)
                        context.updateContent({
                            context.previousContent()
                        })
                    }
                ) {
                    Text(
                        text = "Mocha",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
    BackHandler(true) {
        (context as EsjZoneActivity).updateContent({
            context.previousContent()
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(context: Context?, extra: @Composable () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Text(
                text = "设置",
                fontSize = 6.em,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                shape = RoundedCornerShape(5.dp)
            ) {
                Row {
                    Text(
                        text = "使用代理 (暂未开放)",
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Switch(checked = false,
                        modifier = Modifier.padding(
                            top = 2.dp,
                            start = 0.dp,
                            bottom = 2.dp,
                            end = 8.dp
                        ),
                        onCheckedChange = {
                            // TODO
                        })
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    (context as EsjZoneActivity).updateContentWithoutUpdate({
                        context.previousContent()
                        ThemeDialog(context = context)
                    })
                }
            ) {
                Text(
                    text = "修改主题",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    extra()
}