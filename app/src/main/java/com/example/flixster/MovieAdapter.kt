package com.example.flixster

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


private const val TAG = "Akosah"
const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter(private val context: Context, private val movies:
List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView), View.OnClickListener{
         private val  ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
         private val  tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
         private val  tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)

         init{
             itemView.setOnClickListener(this)
         }
        fun bind(movie: Movie){
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            if(context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i(TAG, "Phone is in portrait mode")
                val radius = 10;
                val margin = 2;
                Glide.with(context)
                    .load("http://via.placeholder.com/300.png")
                    .placeholder(R.drawable.placeholder)
                    .into(ivPoster)
                Glide.with(context).load(movie.posterImageUrl).transform(CenterInside(), RoundedCorners(24))
                    .into(ivPoster)
            }else if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.i(TAG, "Phone is in landscape mode")
                Glide.with(context).load(movie.backdropImageUrl).into(ivPoster)
            }

        }

        override fun onClick(p0: View?) {
            val movie = movies[adapterPosition]
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (context as MainActivity),
                (ivPoster as View?)!!, "profile"
            )
            context.startActivity(intent, options.toBundle())
        }
    }

}
