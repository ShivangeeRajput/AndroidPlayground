package com.example.androidplayground.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidplayground.UserViewModel

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel()
) {
    LazyColumn(modifier = modifier) {

        itemsIndexed(viewModel.users, key = { _, user -> user.id }) { index, user ->
            // Pagination: when the last item shows up, load the next page
            if (index == viewModel.users.lastIndex) {
                LaunchedEffect(Unit) { viewModel.loadNextPage() }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("${user.firstName} ${user.lastName}")
                    Text(user.gender)
                }
            }
        }

        item {
            if (viewModel.isLoading) CircularProgressIndicator(Modifier.padding(16.dp))
            viewModel.error?.let {
                Text(it, Modifier.padding(16.dp))
                Button(onClick = viewModel::loadNextPage) { Text("Retry") }
            }
        }
    }
}
