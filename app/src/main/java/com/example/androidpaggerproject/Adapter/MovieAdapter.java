package com.example.androidpaggerproject.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.androidpaggerproject.databinding.SignalMovieItemBinding;
import com.example.androidpaggerproject.model.Movie;

import org.jetbrains.annotations.NotNull;

public class MovieAdapter extends PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder> {
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int MOVIE_ITEM = 1;
    RequestManager glide;
    public MovieAdapter(@NotNull DiffUtil.ItemCallback<Movie> diffCallback, RequestManager glide) {
        super(diffCallback);
        this.glide = glide;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return MovieViewHolder
        return new MovieViewHolder(SignalMovieItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Get current movie
        Movie currentMovie = getItem(position);
        // Check for null
        if (currentMovie != null) {
            // Set Image of Movie using glide Library
            glide.load("https://image.tmdb.org/t/p/w500" + currentMovie.getPosterPath())
                    .into(holder.movieItemBinding.imageViewMovie);

            // Set rating of movie
            holder.movieItemBinding.textViewRating.setText(String.valueOf(currentMovie.getVoteAverage()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? MOVIE_ITEM : LOADING_ITEM;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        // Define movie_item layout view binding
        SignalMovieItemBinding movieItemBinding;

        public MovieViewHolder(@NonNull SignalMovieItemBinding movieItemBinding) {
            super(movieItemBinding.getRoot());
            // init binding
            this.movieItemBinding = movieItemBinding;
        }
    }

}