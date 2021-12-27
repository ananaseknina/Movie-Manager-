package com.company;

public class Movie {
    String imdb_id;
    String title;
    int year;

    public String[] getTableValues(){
        String[] tableValues = {imdb_id, title, String.valueOf(year)};
        return tableValues;
    }
}
