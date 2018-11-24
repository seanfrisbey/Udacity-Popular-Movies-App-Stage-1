package com.persistentdevelopment.watchit.interfaces;

import com.persistentdevelopment.watchit.objects.Movie;

public interface AsyncResponse {
    void processFinish(Movie[] movies);
}
