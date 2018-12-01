package com.persistentdevelopment.watchit.utilities;

import android.util.Log;

import com.persistentdevelopment.watchit.objects.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TmdbUtils {

    static final String TAG = "TmdbUtils";

    public enum QueryMode {
        DISCOVER,
        MOVIES_NOW_PLAYING,
        MOVIES_POPULAR,
        MOVIES_TOP_RATED,
        MOVIES_UPCOMING
    }

    public static final class PosterSizes {
        public static final String ORIGINAL = "original";
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
    }

    public static Movie[] getMoviesFromJson(String json, QueryMode queryMode)
            throws JSONException {

        final String TMDB_STATUS_CODE = "status_code";
        final String TMDB_STATUS_MESSAGE = "status_message";

        final String TMDB_RESULTS = "results";
        final String TMDB_ID = "id";
        final String TMDB_RATING = "vote_average";
        final String TMDB_TITLE = "title";
        final String TMDB_POPULARITY = "popularity";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";

        Movie[] movies = new Movie[]{};
        JSONObject moviesJson = new JSONObject(json);

        if (moviesJson.has(TMDB_STATUS_CODE)) {
            int statusCode = moviesJson.getInt(TMDB_STATUS_CODE);
            String statusMessage = moviesJson.getString(TMDB_STATUS_MESSAGE);

            Log.e(TAG, String.format("%s error - %s", statusCode, statusMessage));
            return movies;
        }

        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

        movies = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

            int Id = movie.getInt(TMDB_ID);
            String Title = movie.getString(TMDB_TITLE);
            double Popularity = movie.getDouble(TMDB_POPULARITY);
            double Rating = movie.getDouble(TMDB_RATING);
            String Overview = movie.getString(TMDB_OVERVIEW);
            String ReleaseDate = movie.getString(TMDB_RELEASE_DATE);
            String PosterPath = movie.getString(TMDB_POSTER_PATH);

            Movie parsedMovie = new Movie();

            parsedMovie.setId(Id);
            parsedMovie.setTitle(Title);
            parsedMovie.setPopularity(Popularity);
            parsedMovie.setRating(Rating);
            parsedMovie.setOverview(Overview);
            parsedMovie.setReleaseDate(ReleaseDate);
            parsedMovie.setPosterPath(PosterPath);

            movies[i] = parsedMovie;
        }

        return movies;
    }
}
