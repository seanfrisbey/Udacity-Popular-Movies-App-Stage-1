package com.persistentdevelopment.watchit.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Movie> mMovies;

    public MovieAdapter(Context context) {
        super(context, 0);
        mContext = context;
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
                Intent intent = new Intent(mContext, MovieDetailView.class);
                intent.putExtra("movie", movie);
                mContext.startActivity(intent);
            }
        });
        Picasso.get()
                .load(movie.getPosterPath())
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.error_poster)
                .into(posterView);

        return convertView;
    }

    public void updateData(List<Movie> newMovies) {
        mMovies = newMovies;
        notifyDataSetChanged();
    }

    public List<Movie> GetMovies() { return mMovies; }
}
