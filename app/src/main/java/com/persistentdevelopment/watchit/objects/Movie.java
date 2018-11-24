package com.persistentdevelopment.watchit.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.persistentdevelopment.watchit.utilities.NetworkUtils;
import com.persistentdevelopment.watchit.utilities.TmbdUtils.PosterSizes;

import java.net.URL;
import java.util.Comparator;
import java.util.Date;

public class Movie implements Comparable<Movie>, Parcelable {

    private int Id;
    private String Title;
    private double Popularity;
    private double Rating;
    private String Overview;
    private Date ReleaseDate;
    private String PosterPath;

    public Movie() {}

    protected Movie(Parcel in) {
        Id = in.readInt();
        Title = in.readString();
        Popularity = in.readDouble();
        Rating = in.readDouble();
        Overview = in.readString();
        ReleaseDate = new Date(Date.parse(in.readString()));
        PosterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() { return Id; }
    public void setId(int value) { Id = value; }

    public String getTitle() { return Title; }
    public void setTitle(String value) { Title = value; }

    public double getPopularity() { return Popularity; }
    public void setPopularity(double value) { Popularity = value; }

    public double getRating() { return Rating; }
    public void setRating(double value) { Rating = value; }

    public String getOverview() { return Overview; }
    public void setOverview(String value) { Overview = value; }

    public Date getReleaseDate() { return ReleaseDate; }
    public void setReleaseDate(String value) {
        String[] dateParts = value.split("-");
        ReleaseDate = new Date(
                Integer.parseInt(dateParts[0]) - 1900,
                Integer.parseInt(dateParts[1]) - 1,
                Integer.parseInt(dateParts[2]));
    }

    public String getPosterPath() {
        String scheme = "http";
        String authority = "image.tmdb.org";
        String[] paths = new String[]{"t", "p", PosterSizes.W185, PosterPath};

        URL url = NetworkUtils.buildUrl(scheme, authority, paths, null);
        if (url == null) return null;

        return url.toString();
    }

    public void setPosterPath(String value) {
        PosterPath = value.startsWith("/")
            ? value.substring(1)
            : value;
    }

    @Override
    public int compareTo(@NonNull Movie movie) {
        return 0;
    }

    public static Comparator<Movie> MovieTitleComparator
            = new Comparator<Movie>() {

        public int compare(Movie movie1, Movie movie2) {

            String movieTitle1 = movie1.getTitle().toUpperCase();
            String movieTitle2 = movie2.getTitle().toUpperCase();

            return movieTitle1.compareTo(movieTitle2);
        }
    };

    public static Comparator<Movie> MovieDateComparator
            = new Comparator<Movie>() {

        public int compare(Movie movie1, Movie movie2) {

            Date movieDate1 = movie1.getReleaseDate();
            Date movieDate2 = movie2.getReleaseDate();

            return movieDate2.compareTo(movieDate1);
        }
    };

    public static Comparator<Movie> MovieRatingComparator
            = new Comparator<Movie>() {

        public int compare(Movie movie1, Movie movie2) {

            Double movieRating1 = movie1.getRating();
            Double movieRating2 = movie2.getRating();


            return movieRating2.compareTo(movieRating1);
        }
    };

    public static Comparator<Movie> MoviePopularityComparator
            = new Comparator<Movie>() {

        public int compare(Movie movie1, Movie movie2) {

            Double moviePopularity1 = movie1.getPopularity();
            Double moviePopularity2 = movie2.getPopularity();

            return moviePopularity2.compareTo(moviePopularity1);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Title);
        parcel.writeDouble(Popularity);
        parcel.writeDouble(Rating);
        parcel.writeString(Overview);
        parcel.writeString(ReleaseDate.toString());
        parcel.writeString(PosterPath);
    }
}
