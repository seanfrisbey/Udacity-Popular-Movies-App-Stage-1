package com.persistentdevelopment.watchit.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.persistentdevelopment.watchit.R;
import com.persistentdevelopment.watchit.adapters.MovieAdapter;
import com.persistentdevelopment.watchit.objects.Movie;
import com.persistentdevelopment.watchit.interfaces.AsyncResponse;
import com.persistentdevelopment.watchit.utilities.NetworkUtils;
import com.persistentdevelopment.watchit.utilities.TmbdUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    static public String TAG = "MainActivity";
    static private final String API_KEY = "YOUR_KEY_HERE";
    RetrieveMoviesTask moviesTask = new RetrieveMoviesTask();
    MovieAdapter mAdapter;

    GridView gridView;
    ProgressBar progress;

    enum SortMode {
        DATE, POPULARITY, RATING, NAME,
    }

    private MenuItem mSortByDate;
    private MenuItem mSortByPopularity;
    private MenuItem mSortByRating;
    private MenuItem mSortByName;
    private SortMode mSortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = findViewById(R.id.movie_grid);
        gridView.setVisibility(View.INVISIBLE);

        progress = findViewById(R.id.loader_progress);
        progress.setVisibility(View.VISIBLE);

        moviesTask.delegate = this;
        moviesTask.execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSortByDate.setEnabled(mSortMode != SortMode.DATE);
        mSortByPopularity.setEnabled(mSortMode != SortMode.POPULARITY);
        mSortByRating.setEnabled(mSortMode != SortMode.RATING);
        mSortByName.setEnabled(mSortMode != SortMode.NAME);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mSortByDate = menu.findItem(R.id.action_sort_by_date);
        mSortByPopularity = menu.findItem(R.id.action_sort_by_popularity);
        mSortByRating = menu.findItem(R.id.action_sort_by_rating);
        mSortByName = menu.findItem(R.id.action_sort_by_name);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_by_date:
                SetSortMode(SortMode.DATE);
                return true;
            case R.id.action_sort_by_popularity:
                SetSortMode(SortMode.POPULARITY);
                return true;
            case R.id.action_sort_by_rating:
                SetSortMode(SortMode.RATING);
                return true;
            case R.id.action_sort_by_name:
                SetSortMode(SortMode.NAME);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void processFinish(Movie[] movies) {
        mAdapter = new MovieAdapter(this);
        mAdapter.updateData(Arrays.asList(movies));
        SetSortMode(SortMode.DATE);
        gridView.setAdapter(mAdapter);

        progress.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }

    void SetSortMode(SortMode sortMode){
        mSortMode = sortMode;
        if (mAdapter == null) return;
        List<Movie> movies = mAdapter.GetMovies();

        switch (mSortMode)
        {
            case DATE:
                movies.sort(Movie.MovieDateComparator);
                break;
            case POPULARITY:
                movies.sort(Movie.MoviePopularityComparator);
                break;
            case RATING:
                movies.sort(Movie.MovieRatingComparator);
                break;
            case NAME:
                movies.sort(Movie.MovieTitleComparator);
                break;
        }

        mAdapter.updateData(movies);
        Toast.makeText(this, "Sorting by " + mSortMode.name()
                .toLowerCase(), Toast.LENGTH_LONG).show();
    }

    static class RetrieveMoviesTask extends AsyncTask<Void, Void, Movie[]>{
        String TAG = "MovieTask";
        AsyncResponse delegate = null;

        @Override
        protected Movie[] doInBackground(Void... voids) {
            String scheme = "https";
            String authority = "api.themoviedb.org";
            String[] paths = new String[]{"3", "discover", "movie"};
            Hashtable<String, String> params = new Hashtable<String, String>(){{
                put("api_key", API_KEY);
                put("language", "en-US");
                put("include_adult", "false");
            }};

            URL url = NetworkUtils.buildUrl(scheme, authority, paths, params);
            if (url == null) return null;

            String json = NetworkUtils.getResponseFromHttpUrl(url);
            if (json == null) return null;

            Movie[] movies = null;
            try {
                movies = TmbdUtils.getMoviesFromJson(json);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            return movies;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            delegate.processFinish(movies);
        }
    }
}
