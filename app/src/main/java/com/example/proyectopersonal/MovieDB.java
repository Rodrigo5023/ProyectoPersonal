package com.example.proyectopersonal;

import android.app.Application;

// https://stackoverflow.com/questions/18002227/why-extend-the-android-application-class
public class MovieDB extends Application {

    //Parámetros Película
    public static final String urlMovieDB = "https://api.themoviedb.org/3/";
    public static final String ApiKey = "06a1953c26075c04668b820d78955ec7";
    public static final String urlPhoto = " https://image.tmdb.org/t/p/w600_and_h900_bestv2";

    // Parámetros Trailer
    public static final String youtube = "https://www.youtube.com/watch?v=";
    public static final String trailerImageUrl = "http://i1.ytimg.com/vi/";



}

