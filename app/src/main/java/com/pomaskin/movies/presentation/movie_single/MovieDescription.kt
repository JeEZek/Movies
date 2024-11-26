package com.pomaskin.movies.presentation.movie_single

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pomaskin.movies.domain.entity.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDescription(
    onBackPressed: () -> Unit,
    movie: Movie
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBackPressed() },
        onClick = {
            onBackPressed()
        }
    ) {
        Text(text = movie.title)
    }
}
