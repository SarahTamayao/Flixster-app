package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers


private const val YOUTUBE_API_KEY = "AIzaSyATbEx3hlpUD_bZh-UhYXpCsxnwgEV2k0o"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TAG = "Youtube"
class MovieDetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        tvTitle = findViewById(R.id.tvTitle)
        tvOverview =findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAverage)
        ytPlayerView = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i("Detail", "Movie is $movie")
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieID), object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure, $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val results = json.jsonObject.getJSONArray("results")
                if(results.length()==0){
                    Log.i(TAG, "No movies found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                initializeYoutube(youtubeKey, movie)

            }

        })



    }

    private fun initializeYoutube(youtubeKey: String, movie: Movie) {

        ytPlayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                if(movie.voteAverage>=5){
                    player?.loadVideo(youtubeKey)
                    player?.play()
                }else if(movie.voteAverage<5){
                    player?.cueVideo(youtubeKey);
                }

                Log.i("GiveThem","It  loaded")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i("GiveThem","It didnt load")
            }

        })


    }
}