package com.persistentdevelopment.watchit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.persistentdevelopment.watchit.R;
import com.persistentdevelopment.watchit.objects.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailView extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_view);

        Intent i = getIntent();
        mMovie = i.getParcelableExtra("movie");

        Picasso.get().load(mMovie.getPosterPath())
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.error_poster)
                .into((ImageView)findViewById(R.id.movie_detail_poster));
        ((TextView) findViewById(R.id.movie_detail_year)).setText(String.valueOf(mMovie.getReleaseDate().getYear()+1900));
        ((TextView) findViewById(R.id.movie_detail_runtime)).setText(String.valueOf(mMovie.getRuntime()) + " minutes");
        ((TextView) findViewById(R.id.movie_detail_rating)).setText(String.valueOf(mMovie.getRating()) + "/10");
        ((TextView) findViewById(R.id.movie_detail_overview)).setText(mMovie.getOverview());
    }
}
