package net.deechael.esjzone.compose

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.compose.general.Home
import net.deechael.esjzone.compose.general.Loading
import net.deechael.esjzone.compose.general.Login
import net.deechael.esjzone.compose.novel.CategoryPage
import net.deechael.esjzone.compose.novel.NovelPage

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Start(context: Context?, startAt: String = "boostrap") {
    context as EsjZoneActivity
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startAt
    ) {
        composable("boostrap") {
            Loading()
            GlobalScope.launch {
                if (context.esjzone.isLoggedIn()) {
                    MainScope().launch {
                        navController.navigate(route = "home")
                    }
                } else {
                    MainScope().launch {
                        context.toast("您没有登录")
                        navController.navigate(route = "login")
                    }
                }
            }
        }
        composable("home") {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(selected = selectedIndex == 0,
                            enabled = selectedIndex != 0,
                            onClick = {
                                selectedIndex = 0
                            }, label = {
                                Text(
                                    text = "主页"
                                )
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = "主页"
                                )
                            })
                        NavigationBarItem(selected = selectedIndex == 1,
                            enabled = selectedIndex != 1,
                            onClick = {
                                selectedIndex = 1
                            }, label = {
                                Text(
                                    text = "搜索"
                                )
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "搜索"
                                )
                            })
                        NavigationBarItem(selected = selectedIndex == 2,
                            enabled = selectedIndex != 2,
                            onClick = {
                                selectedIndex = 2
                            }, label = {
                                Text(
                                    text = "个人"
                                )
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "个人"
                                )
                            })
                        NavigationBarItem(
                            selected = selectedIndex == 3,
                            enabled = selectedIndex != 3,
                            onClick = {
                                selectedIndex = 3
                            },
                            label = {
                                Text(
                                    text = "设置"
                                )
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "设置"
                                )
                            })
                    }
                }
            ) {
                if (selectedIndex == 0) {
                    Column {
                        Home(context, navController)
                    }
                }
            }
        }
        composable("category") {
            CategoryPage(
                context = context,
                category = EsjZoneActivity.viewing.category,
                navController = navController
            )
        }
        composable("novel") {
            NovelPage(
                context = context,
                novel = EsjZoneActivity.viewing.novel,
                navController = navController
            )
        }
        composable("login") {
            Login(context = context) {
                navController.navigate(route = "home")
            }
        }
    }
}