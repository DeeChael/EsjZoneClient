package net.deechael.compose.novel.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

interface ChapterContent {

    fun toComposable(): @Composable () -> Unit

}

class ChapterText(content: String) : ChapterContent {

    private val content: String

    init {
        this.content = content
    }

    override fun toString(): String {
        return this.content
    }

    override fun toComposable(): @Composable () -> Unit {
        return {
            Text(
                text = this.content,
                modifier = Modifier.padding(4.dp)
            )
        }
    }

}

class ChapterBreakLine : ChapterContent {

    override fun toString(): String {
        return "\n"
    }

    override fun toComposable(): @Composable () -> Unit {
        return {
            Text(
                text = "",
                modifier = Modifier.padding(2.dp)
            )
        }
    }

}

class ChapterImage(url: String) : ChapterContent {

    private val url: String

    init {
        this.url = url
    }

    override fun toString(): String {
        return this.url
    }

    override fun toComposable(): @Composable () -> Unit {
        return {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(this.url)
                    .crossfade(true)
                    .build(),
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = null,
                modifier = Modifier.padding(2.dp)
            )
        }
    }

}