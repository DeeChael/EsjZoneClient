package net.deechael.esjzone.page

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.NoInspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.item.Category
import net.deechael.esjzone.thread
import net.deechael.esjzone.ui.theme.ESJZoneTheme

@Preview
@Composable
fun ContainerPreview() {
    Container {
        Login(context = null)
    }
}

@Composable
fun MainPage(context: Context, categories: List<Category>) {
    Container(
        userTrigger = {
            (context as MainActivity).setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Loading()
                    }
                }
            }
            context.setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        UserPage(context = context)
                    }
                }
            }
        },
        settingsTrigger = {
            (context as MainActivity).setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Loading()
                    }
                }
            }
            context.setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        SettingsPage(context = context)
                    }
                }
            }
        }
    ) {
        Categories(context, categories)
    }
}

@Composable
fun UserPage(context: Context) {
    Container(
        mainPageTrigger = {
            (context as MainActivity).setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Loading()
                    }
                }
            }
            val categories = context.esjzone.getCategories()
            context.setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        MainPage(context = context, categories)
                    }
                }
            }
        },
        settingsTrigger = {
            (context as MainActivity).setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Loading()
                    }
                }
            }
            context.setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        SettingsPage(context = context)
                    }
                }
            }
        }
    ) {
        val network = (context as MainActivity).esjzone
        User(context = context, username = context.username)
    }
}

@Composable
fun SettingsPage(context: Context) {
    Container(
        mainPageTrigger = {
            (context as MainActivity).setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Loading()
                    }
                }
            }
            val categories = context.esjzone.getCategories()
            context.setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        MainPage(context = context, categories)
                    }
                }
            }
        },
        userTrigger = {
            (context as MainActivity).setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Loading()
                    }
                }
            }
            context.setContent {
                ESJZoneTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        UserPage(context = context)
                    }
                }
            }
        }
    ) {
        val network = (context as MainActivity).esjzone
        Settings(context = context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(
    mainPageTrigger: () -> Unit = {},
    userTrigger: () -> Unit = {},
    settingsTrigger: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            BoxScopeInstance.content()
        }
        Divider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
        Column(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    onClick = {
                        GlobalScope.launch {
                            thread {
                                mainPageTrigger()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxHeight(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "主页",
                        modifier = Modifier.padding(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(
                    onClick = {
                        GlobalScope.launch {
                            thread {
                                userTrigger()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxHeight(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "个人",
                        modifier = Modifier.padding(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(
                    onClick = {
                        GlobalScope.launch {
                            thread {
                                settingsTrigger()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxHeight(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "设置",
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        }
    }

}

internal object BoxScopeInstance : BoxScope {
    @Stable
    override fun Modifier.align(alignment: Alignment) = this.then(
        BoxChildData(
            alignment = alignment,
            matchParentSize = false,
            inspectorInfo = debugInspectorInfo {
                name = "align"
                value = alignment
            }
        )
    )

    @Stable
    override fun Modifier.matchParentSize() = this.then(
        BoxChildData(
            alignment = Alignment.Center,
            matchParentSize = true,
            inspectorInfo = debugInspectorInfo { name = "matchParentSize" }
        )
    )
}


private class BoxChildData(
    var alignment: Alignment,
    var matchParentSize: Boolean = false,
    inspectorInfo: InspectorInfo.() -> Unit = NoInspectorInfo
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
    override fun Density.modifyParentData(parentData: Any?) = this@BoxChildData

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifier = other as? BoxChildData ?: return false

        return alignment == otherModifier.alignment &&
                matchParentSize == otherModifier.matchParentSize
    }

    override fun hashCode(): Int {
        var result = alignment.hashCode()
        result = 31 * result + matchParentSize.hashCode()
        return result
    }

    override fun toString(): String =
        "BoxChildData(alignment=$alignment, matchParentSize=$matchParentSize)"
}