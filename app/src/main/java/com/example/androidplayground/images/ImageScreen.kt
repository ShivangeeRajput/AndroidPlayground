package com.example.androidplayground.images

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.androidplayground.core.UiState
import org.koin.compose.koinInject

/**
 * produceState cancels and restarts when the url changes or the item leaves the screen,
 * so a recycled cell never shows the wrong image.
 */
@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier, loader: ImageLoader<Bitmap> = koinInject()) {
    var attempt by remember(url) { mutableIntStateOf(0) }

    key(url, attempt) {
        val state by produceState<UiState<Bitmap>>(UiState.Loading) {
            value = loader.load(url).fold(
                onSuccess = { UiState.Success(it) },
                onFailure = {
                    Log.e("ImageLoader", "failed: $url", it)
                    UiState.Error("Failed")
                },
            )
        }
        Box(modifier, contentAlignment = Alignment.Center) {
            when (val s = state) {
                UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> TextButton(onClick = { attempt++ }) { Text("Retry") }
                is UiState.Success -> Image(
                    bitmap = s.data.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
fun ImageGridScreen() {
    val urls = remember { List(60) { "https://picsum.photos/id/${it + 10}/600/600" } }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(urls, key = { it }) { NetworkImage(it, Modifier.aspectRatio(1f)) }
    }
}
