package com.example.proyectopersonal;

import android.app.Application;

public class MovieDB {

    //Parámetros Película
    private static final String urlMovieDB = "https://api.themoviedb.org/3/";
    private static final String ApiKey = "06a1953c26075c04668b820d78955ec7";
    private static final String urlPhoto = " https://image.tmdb.org/t/p/w600_and_h900_bestv2";

    // Parámetros Trailer
    private static final String youtube = "https://www.youtube.com/watch?v=";
    private static final String trailerImageUrl = "http://i1.ytimg.com/vi/";


    public  String getUrlMovieDB() { return urlMovieDB; }
    public  String getApiKey() { return ApiKey; }
    public  String getUrlPhoto() { return urlPhoto; }
    public  String getYoutube() { return youtube; }
    public  String getTrailerImageUrl() { return trailerImageUrl; }

}

