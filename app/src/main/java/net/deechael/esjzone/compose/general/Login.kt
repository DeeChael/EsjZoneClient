package net.deechael.esjzone.compose.general

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.deechael.esjzone.EsjZoneActivity
import net.deechael.esjzone.client.EsjzoneLoginer


@Preview
@Composable
fun LoginPreview() {
    Login(null)
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Login(context: Context?, onSuccess: () -> Unit = {}) {
    context as EsjZoneActivity

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var buttonEnabled by rememberSaveable { mutableStateOf(true) }

    var tryLogging by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "请登录",
            modifier = Modifier.padding(16.dp),
            fontSize = 6.em
        )
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text("邮箱")
            },
            shape = RoundedCornerShape(5.dp),
            singleLine = true,
            placeholder = {
                Text("密码")
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text("密码")
            },
            shape = RoundedCornerShape(5.dp),
            singleLine = true,
            placeholder = {
                Text("密码")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "隐藏密码" else "显示密码"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = {
            buttonEnabled = false
            tryLogging = true
            Toast.makeText(context, "登录中", Toast.LENGTH_SHORT).show()
            GlobalScope.launch {
                context.esjzone = EsjzoneLoginer.of().login(email, password)
                if (context.esjzone.isLoggedIn()) {
                    MainScope().launch {
                        onSuccess()
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    MainScope().launch {
                        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show()
                    }
                    tryLogging = false
                    buttonEnabled = true
                }
            }
        },
            enabled = buttonEnabled,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.clickable { buttonEnabled }
        ) {
            Text("登录")
        }
        if (tryLogging) {
            Popup {
                Indicator()
            }
        }
    }

}