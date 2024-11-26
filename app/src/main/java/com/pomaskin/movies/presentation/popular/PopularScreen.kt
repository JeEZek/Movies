package com.pomaskin.movies.presentation.popular

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.getApplicationComponent

@Composable
fun PopularScreen(
    paddingValues: PaddingValues,
    onMovieCardClickListener: (Movie) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: PopularViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(PopularScreenState.Initial)

    PopularScreenContent(
        viewModel = viewModel,
        screenState = screenState,
        paddingValues = paddingValues,
        onMovieCardClickListener = onMovieCardClickListener,
    )
}

@Composable
private fun PopularScreenContent(
    viewModel: PopularViewModel,
    screenState: State<PopularScreenState>,
    paddingValues: PaddingValues,
    onMovieCardClickListener: (Movie) -> Unit
) {
    when (val currentState = screenState.value) {
        is PopularScreenState.Movies -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PopularMovies(
                    viewModel = viewModel,
                    screenState = screenState,
                    paddingValues = paddingValues,
                    movies = currentState.movies,
                    onMovieClickListener = onMovieCardClickListener,
                    nextDataIsLoading = currentState.nextDataIsLoading
                )
            }
        }

        is PopularScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        PopularScreenState.Initial -> {}
    }
}

@Composable
fun PopularMovies(
    viewModel: PopularViewModel,
    screenState: State<PopularScreenState>,
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
                screenState = screenState,
                paddingValues = paddingValues,
                movie = it,
                onMovieClickListener = onMovieClickListener,
                nextDataIsLoading = nextDataIsLoading
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
                }
                Log.d("Test_loading", "loading")
            } else {
                SideEffect {
                    viewModel.loadNextPopular()
                }
                Log.d("Test_loading", "Side effect")
            }
        }
    }
}

@Composable
fun MovieCard(
    screenState: State<PopularScreenState>,
    paddingValues: PaddingValues,
    movie: Movie,
    onMovieClickListener: (Movie) -> Unit,
    nextDataIsLoading: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onMovieClickListener(movie) },
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = movie.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500" + movie.posterPath,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentDescription = "Movie Poster",
                contentScale = ContentScale.Crop
            )
        }
    }
}



