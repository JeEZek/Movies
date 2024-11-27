package com.pomaskin.movies.presentation.favourite

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.getApplicationComponent

@Composable
fun FavouriteScreen(
    paddingValues: PaddingValues,
    onMovieCardClickListener: (Movie) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: FavouriteViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(FavouriteScreenState.Initial)


    FavouriteScreenContent(
        viewModel = viewModel,
        screenState = screenState,
        paddingValues = paddingValues,
        onMovieCardClickListener = onMovieCardClickListener,
    )
}

@Composable
private fun FavouriteScreenContent(
    viewModel: FavouriteViewModel,
    screenState: State<FavouriteScreenState>,
    paddingValues: PaddingValues,
    onMovieCardClickListener: (Movie) -> Unit
) {
    when (val currentState = screenState.value) {
        is FavouriteScreenState.Movies -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FavouriteMovies(
                    viewModel = viewModel,
                    screenState = screenState,
                    paddingValues = paddingValues,
                    movies = currentState.movies,
                    onMovieClickListener = onMovieCardClickListener,
                    nextDataIsLoading = currentState.nextDataIsLoading
                )
            }
        }

        is FavouriteScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        FavouriteScreenState.Initial -> {}
    }
}

@Composable
fun FavouriteMovies(
    viewModel: FavouriteViewModel,
    screenState: State<FavouriteScreenState>,
    paddingValues: PaddingValues,
    movies: List<Movie>,
    onMovieClickListener: (Movie) -> Unit,
    nextDataIsLoading: Boolean
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
    ) {
        items(
            items = movies,
            key = { it.id }
        ) {
            MovieCard(
                paddingValues = paddingValues,
                movie = it,
                onMovieClickListener = onMovieClickListener,
            )
        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                    Log.d("loadingNextFavourite", "data ${screenState.value}")

                }
            } else {
                SideEffect {
                    viewModel.loadNextFavourite()
                    Log.d("loadingNextFavourite", "FavouriteScreen")
                    Log.d("loadingNextFavourite", "data ${screenState.value}")
                }
            }
        }
    }
}


@Composable
fun MovieCard(
    paddingValues: PaddingValues,
    movie: Movie,
    onMovieClickListener: (Movie) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onMovieClickListener(movie) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Используем правильный метод
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // Movie Poster
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500" + movie.posterPath,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentDescription = "Movie Poster",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Movie Title
            Text(
                text = movie.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Overview
            Text(
                text = movie.overview,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating and Vote Count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${movie.voteAverage} (${movie.voteCount} votes)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
