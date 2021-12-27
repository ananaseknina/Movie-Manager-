package com.company;

import java.util.Arrays;

public class MovieDetails {
    String[] countries;
    String[] directors;
    String[] genres;
    String imdb_id;
    String imdb_rating;
    String[] language;
    String popularity;
    String rated;
    String release_date;
    String runtime;
    String[] stars;
    String title;
    String vote_count;
    String year;
    String youtube_trailer_key;

    public String[] getTableValues(){

        String[] tableValues = {
                Arrays.toString(countries),
                Arrays.toString(directors),
                Arrays.toString(genres),
                imdb_id,
                imdb_rating,
                Arrays.toString(language),
                popularity,
                rated,
                release_date,
                runtime,
                Arrays.toString(stars),
                title,
                vote_count,
                year,
                youtube_trailer_key};

        return tableValues;
    }
}
