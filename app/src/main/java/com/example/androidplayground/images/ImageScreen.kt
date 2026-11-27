package com.example.androidplayground.images

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.CancellationException
import org.koin.compose.koinInject

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier, loader: ImageLoader = koinInject()) {
    // remember(url): url badle to purani image reset
    var bitmap by remember(url) { mutableStateOf<Bitmap?>(null) }
    var failed by remember(url) { mutableStateOf(false) }

    // Item screen se hata to coroutine cancel
    LaunchedEffect(url) {
        try {
            bitmap = loader.load(url)
            failed = bitmap == null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("ImageLoader", "failed: $url", e)
            failed = true
        }
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        val image = bitmap
        when {
            image != null -> Image(
                image.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            failed -> Text("Failed")
            else -> CircularProgressIndicator()
        }
    }
}

@Composable
fun ImageGridScreen(modifier: Modifier = Modifier) {
    val urls = List(40) { "https://picsum.photos/id/${it + 10}/600/600" }

    LazyVerticalGrid(GridCells.Fixed(2), modifier) {
        items(urls) { NetworkImage(it, Modifier.aspectRatio(1f)) }
    }
}
