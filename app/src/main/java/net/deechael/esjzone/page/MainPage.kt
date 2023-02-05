package net.deechael.esjzone.page

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.item.Category
import net.deechael.esjzone.thread

@Preview
@Composable
fun ContainerPreview() {
    Container(index = -1) {
        Login(context = null)
    }
}

@Preview
@Composable
fun MainPagePreview() {
    Container(
        userTrigger = {
        },
        settingsTrigger = {
        },
        bottomBar = true,
        index = 0
    ) {
        Categories(
            null, listOf(
                Category("Test1", ""),
                Category("Test2", ""),
                Category("Test3", ""),
                Category("Test4", ""),
                Category("Test5", ""),
                Category("Test6", ""),
                Category("Test7", ""),
                Category("Test8", "")
            )
        )
    }
}

@Composable
fun MainPage(context: Context?, categories: List<Category>) {
    Container(
        userTrigger = {
            (context as EsjZoneActivity).updateContent({
                Loading()
            })
            context.updateContent({
                UserPage(context = context)
            })
        },
        settingsTrigger = {
            (context as EsjZoneActivity).updateContent({
                Loading()
            })
            context.updateContent({
                SettingsPage(context = context)
            })
        },
        bottomBar = true,
        index = 0
    ) {
        Categories(context, categories)
    }
}

@Composable
fun UserPage(context: Context) {
    Container(
        mainPageTrigger = {
            (context as EsjZoneActivity).updateContent({
                Loading()
            })
            val categories = context.esjzone.getCategories()
            context.updateContent({
                MainPage(context = context, categories)
            })
        },
        settingsTrigger = {
            (context as EsjZoneActivity).updateContent({
                Loading()
            })
            context.updateContent({
                SettingsPage(context = context)
            })
        },
        bottomBar = true,
        index = 1
    ) {
        val network = (context as EsjZoneActivity).esjzone
        User(context = context, username = context.username)
    }
}

@Composable
fun SettingsPage(context: Context) {
    Container(
        mainPageTrigger = {
            (context as EsjZoneActivity).updateContent({
                Loading()
            })
            val categories = context.esjzone.getCategories()
            context.updateContent({
                MainPage(context = context, categories)
            })
        },
        userTrigger = {
            (context as EsjZoneActivity).updateContent({
                Loading()
            })
            context.updateContent({
                UserPage(context = context)
            })
        },
        bottomBar = true,
        index = 2
    ) {
        val network = (context as EsjZoneActivity).esjzone
        Settings(context = context)
    }
}

@Composable
fun NavBar(
    mainPageTrigger: () -> Unit = {},
    userTrigger: () -> Unit = {},
    settingsTrigger: () -> Unit = {},
    selectedIndex: Int = 0
) {
    NavigationBar {
        NavigationBarItem(selected = (selectedIndex == 0), onClick = {
            GlobalScope.launch {
                thread {
                    mainPageTrigger()
                }
            }
        }, label = {
            Text(
                text = "主页",
                modifier = Modifier.padding(18.dp)
            )
        }, icon = {})
        NavigationBarItem(selected = (selectedIndex == 1), onClick = {
            GlobalScope.launch {
                thread {
                    userTrigger()
                }
            }
        }, label = {
            Text(
                text = "个人",
                modifier = Modifier.padding(18.dp)
            )
        }, icon = {})
        NavigationBarItem(selected = (selectedIndex == 2), onClick = {
            GlobalScope.launch {
                thread {
                    settingsTrigger()
                }
            }
        }, label = {
            Text(
                text = "设置",
                modifier = Modifier.padding(18.dp)
            )
        }, icon = {})
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContainer(
    mainPageTrigger: () -> Unit = {},
    userTrigger: () -> Unit = {},
    settingsTrigger: () -> Unit = {},
    index: Int,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        bottomBar = {
            NavBar(mainPageTrigger, userTrigger, settingsTrigger, index)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BoxScopeInstance.content()
        }
    }
}

@Composable
fun Container(
    mainPageTrigger: () -> Unit = {},
    userTrigger: () -> Unit = {},
    settingsTrigger: () -> Unit = {},
    bottomBar: Boolean = false,
    index: Int = -1,
    content: @Composable BoxScope.() -> Unit
) {
    if (bottomBar) {
        NewContainer(mainPageTrigger, userTrigger, settingsTrigger, index) {
            BoxScopeInstance.content()
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                BoxScopeInstance.content()
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