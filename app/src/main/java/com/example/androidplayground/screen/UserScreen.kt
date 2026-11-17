package com.example.androidplayground.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidplayground.viewmodel.UserViewModel


@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel()
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.user, key = {it.id}) { users->
            Text(
                text = "${users.name}",
                modifier = modifier.padding(2.dp)
            )
            Text(
                text = "${users.email}",
                modifier = modifier.padding(2.dp)
            )
            Text(
                text = "${users.phone}",
                modifier = modifier.padding(2.dp)
            )
        }
    }
}