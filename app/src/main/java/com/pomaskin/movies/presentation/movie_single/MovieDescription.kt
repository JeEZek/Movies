package com.pomaskin.movies.presentation.movie_single

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.getApplicationComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MovieDescription(
    onBackPressed: () -> Unit,
    movie: Movie
) {
    val component = getApplicationComponent()
    val viewModel: MovieViewModel = viewModel(factory = component.getViewModelFactory())
    var video by remember { mutableStateOf("") }
    LaunchedEffect(movie) {
        video = viewModel.getVideo(movie)
    }



    MovieDescriptionContent(movie = movie, video = video, viewModel=viewModel)
}

@Composable
fun MovieDescriptionContent(
    movie: Movie,
    video: String,
    viewModel: MovieViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500" + movie.posterPath,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )


                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Add to Favorites",
                        tint = Color(0xFFFFA500),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.changeFavouriteStatus(mediaId = movie.id, favorite = true)
                                }
                            }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.overview,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

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

            if (video.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Video(video = video)
                }
            }
        }
    }
}





@Composable
fun Video(
    video: String,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp)),
        factory = {
            YouTubePlayerView(context = it).apply {
                lifecycle.addObserver(this)

                addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(video, 0f)
                    }
                })
            }
        }
    )
}