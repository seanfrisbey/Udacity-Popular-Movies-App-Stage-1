package com.persistentdevelopment.watchit.utilities;

import com.persistentdevelopment.watchit.objects.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TmbdUtils {

    public static final class PosterSizes {
        public static final String ORIGINAL = "original";
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
    }

    public static Movie[] getMoviesFromJson(String json)
            throws JSONException {

        final String TVDB_STATUS_CODE = "status_code";
        final String TVDB_STATUS_MESSAGE = "status_message";

        final String TVDB_RESULTS = "results";
        final String TVDB_ID = "id";
        final String TVDB_RATING = "vote_average";
        final String TVDB_TITLE = "title";
        final String TVDB_POPULARITY = "popularity";
        final String TVDB_POSTER_PATH = "poster_path";
        final String TVDB_OVERVIEW = "overview";
        final String TVDB_RELEASE_DATE = "release_date";

        Movie[] movies = null;
        JSONObject moviesJson = new JSONObject(json);

        if (moviesJson.has(TVDB_STATUS_CODE)) {
            // TODO
        }

        JSONArray moviesArray = moviesJson.getJSONArray(TVDB_RESULTS);

        movies = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

            int Id = movie.getInt(TVDB_ID);
            String Title = movie.getString(TVDB_TITLE);
            double Popularity = movie.getDouble(TVDB_POPULARITY);
            double Rating = movie.getDouble(TVDB_RATING);
            String Overview = movie.getString(TVDB_OVERVIEW);
            String ReleaseDate = movie.getString(TVDB_RELEASE_DATE);
            String PosterPath = movie.getString(TVDB_POSTER_PATH);

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
