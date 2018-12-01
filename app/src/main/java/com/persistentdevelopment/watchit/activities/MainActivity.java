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

import com.persistentdevelopment.watchit.R;
import com.persistentdevelopment.watchit.adapters.MovieAdapter;
import com.persistentdevelopment.watchit.objects.Movie;
import com.persistentdevelopment.watchit.interfaces.AsyncResponse;
import com.persistentdevelopment.watchit.utilities.TmdbUtils;
import com.persistentdevelopment.watchit.utilities.TmdbUtils.QueryMode;
import com.persistentdevelopment.watchit.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    static private String TAG = "MainActivity";
    static private final String API_KEY = "YOUR_API_KEY";

    private RetrieveMoviesTask moviesTask;
    private MovieAdapter mAdapter;
    private List<Movie> mMovies = new ArrayList<>();

    private GridView gridView;
    private ProgressBar progress;

    private MenuItem mQueryNowPlaying;
    private MenuItem mQueryPopular;
    private MenuItem mQueryTopRated;
    private MenuItem mQueryUpcoming;
    private MenuItem mQueryDiscover;
    private QueryMode mQueryMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = findViewById(R.id.movie_grid);
        gridView.setVisibility(View.INVISIBLE);
        mAdapter = new MovieAdapter(this, mMovies);
        gridView.setAdapter(mAdapter);

        progress = findViewById(R.id.loader_progress);
        progress.setVisibility(View.VISIBLE);

        mQueryMode = QueryMode.MOVIES_NOW_PLAYING;
        SetupNewMoviesTask();
        moviesTask.execute(mQueryMode);
    }

    protected void SetupNewMoviesTask() {
        moviesTask = new RetrieveMoviesTask();
        moviesTask.delegate = this;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mQueryNowPlaying.setEnabled(mQueryMode != QueryMode.MOVIES_NOW_PLAYING);
        mQueryPopular.setEnabled(mQueryMode != QueryMode.MOVIES_POPULAR);
        mQueryTopRated.setEnabled(mQueryMode != QueryMode.MOVIES_TOP_RATED);
        mQueryUpcoming.setEnabled(mQueryMode != QueryMode.MOVIES_UPCOMING);
        mQueryDiscover.setEnabled(mQueryMode != QueryMode.DISCOVER);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mQueryNowPlaying = menu.findItem(R.id.action_query_movies_now_playing);
        mQueryPopular = menu.findItem(R.id.action_query_movies_popular);
        mQueryTopRated = menu.findItem(R.id.action_query_movies_top_rated);
        mQueryUpcoming = menu.findItem(R.id.action_query_movies_upcoming);
        mQueryDiscover = menu.findItem(R.id.action_query_discover);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        moviesTask.cancel(true);

        while (!moviesTask.isCancelled()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int id = item.getItemId();

        progress.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);

        switch (id) {
            case R.id.action_query_movies_now_playing:
                mQueryMode = QueryMode.MOVIES_NOW_PLAYING;
                break;
            case R.id.action_query_movies_popular:
                mQueryMode = QueryMode.MOVIES_POPULAR;
                break;
            case R.id.action_query_movies_top_rated:
                mQueryMode = QueryMode.MOVIES_TOP_RATED;
                break;
            case R.id.action_query_movies_upcoming:
                mQueryMode = QueryMode.MOVIES_UPCOMING;
                break;
            case R.id.action_query_discover:
                mQueryMode = QueryMode.DISCOVER;
                break;
            default:
                return false;
        }

        SetupNewMoviesTask();
        moviesTask.execute(mQueryMode);

        return true;
    }

    @Override
    public void processFinish(Movie[] movies) {
        mMovies.clear();
        mMovies.addAll(Arrays.asList(movies));
        mAdapter.notifyDataSetChanged();

        progress.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }

    static class RetrieveMoviesTask extends AsyncTask<QueryMode, Void, Movie[]>{
        String TAG = "MovieTask";
        AsyncResponse delegate = null;

        @Override
        protected Movie[] doInBackground(QueryMode... queryModes) {
            Movie[] movies = new Movie[]{};
            QueryMode queryMode = queryModes[0];

            String scheme = "https";
            String authority = "api.themoviedb.org";
            String paths = "";
            Hashtable<String, String> params = new Hashtable<String, String>(){{}};

            switch (queryMode)
            {
                case DISCOVER:
                    paths = "3/discover/movie";
                    break;
                case MOVIES_NOW_PLAYING:
                    paths = "3/movie/now_playing";
                    break;
                case MOVIES_POPULAR:
                    paths = "3/movie/popular";
                    break;
                case MOVIES_TOP_RATED:
                    paths = "3/movie/top_rated";
                    break;
                case MOVIES_UPCOMING:
                    paths = "3/movie/upcoming";
                    break;
                default:
                    break;
            }

            params.put("api_key", API_KEY);

            URL url = NetworkUtils.buildUrl(scheme, authority, paths, params);
            if (url == null) return movies;

            String json = NetworkUtils.getResponseFromHttpUrl(url);
            if (json == null) return movies;

            try {
                movies = TmdbUtils.getMoviesFromJson(json, queryMode);
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
