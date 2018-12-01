package com.persistentdevelopment.watchit.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.persistentdevelopment.watchit.R;
import com.persistentdevelopment.watchit.activities.MovieDetailView;
import com.persistentdevelopment.watchit.objects.Movie;
import com.persistentdevelopment.watchit.views.MoviePosterView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private LayoutInflater mInflater;
    private List<Movie> mMovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        mMovies = movies;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Movie movie = getItem(position);
        assert movie != null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.movie_grid_item, parent, false);
        }

        MoviePosterView posterView = convertView.findViewById(R.id.movie_poster_item);
        posterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MovieDetailView.class);
                intent.putExtra("movie", movie);
                getContext().startActivity(intent);
            }
        });
        Picasso.get()
                .load(movie.getPosterPath())
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.error_poster)
                .into(posterView);

        return convertView;
    }

    public List<Movie> GetMovies() { return mMovies; }
}
