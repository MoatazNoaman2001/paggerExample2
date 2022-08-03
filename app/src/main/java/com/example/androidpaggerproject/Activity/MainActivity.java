package com.example.androidpaggerproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.bumptech.glide.RequestManager;
import com.example.androidpaggerproject.Adapter.MovieAdapter;
import com.example.androidpaggerproject.Adapter.MovieLoadStateAdapter;
import com.example.androidpaggerproject.Utils.GridSpace;
import com.example.androidpaggerproject.Utils.MovieComparator;
import com.example.androidpaggerproject.ViewModel.MovieViewModel;
import com.example.androidpaggerproject.databinding.ActivityMainBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    MovieViewModel mainActivityViewModel;
    ActivityMainBinding binding;
    MovieAdapter moviesAdapter;
    @Inject
    RequestManager requestManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create View binding object
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Create new MoviesAdapter object and provide
        moviesAdapter = new MovieAdapter(new MovieComparator(),requestManager);
        // Create ViewModel
        mainActivityViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        //set recyclerview and adapter
        initRecyclerviewAndAdapter();

        // Subscribe to to paging data
        mainActivityViewModel.moviePagingDataFlowable.subscribe(moviePagingData -> {
            // submit new data to recyclerview adapter
            moviesAdapter.submitData(getLifecycle(), moviePagingData);
        });
    }

    private void initRecyclerviewAndAdapter() {
        // Create GridlayoutManger with span of count of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // Finally set LayoutManger to recyclerview
        binding.recyclerViewMovies.setLayoutManager(gridLayoutManager);

        // Add ItemDecoration to add space between recyclerview items
        binding.recyclerViewMovies.addItemDecoration(new GridSpace(2, 20, true));

        // set adapter
        binding.recyclerViewMovies.setAdapter(
                // This will show end user a progress bar while pages are being requested from server
                moviesAdapter.withLoadStateFooter(
                        // When we will scroll down and next page request will be sent
                        // while we get response form server Progress bar will show to end user
                        new MovieLoadStateAdapter(v -> {
                            moviesAdapter.retry();
                        })));
        // set Grid span to set progress at center
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // If progress will be shown then span size will be 1 otherwise it will be 2
                return moviesAdapter.getItemViewType(position) == MovieAdapter.LOADING_ITEM ? 1 : 2;
            }
        });

    }
}