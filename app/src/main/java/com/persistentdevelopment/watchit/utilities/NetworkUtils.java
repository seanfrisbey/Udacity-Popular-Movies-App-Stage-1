package com.persistentdevelopment.watchit.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public final class NetworkUtils {
    static String TAG = "NetworkUtils";

    public static URL buildUrl(String scheme, String authority, String path, Map<String, String> params) {

        Uri.Builder builder = new Uri.Builder()
                .scheme(scheme)
                .authority(authority);

        String[] segments = path.split("/");
        for (String segment : segments) {
            builder.appendPath(segment);
        }

        if (params != null)
        {
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.appendQueryParameter(param.getKey(), param.getValue());
            }
        }

        try {
            return new URL(builder.build().toString());
        } catch (MalformedURLException e){
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return null;
    }
}
