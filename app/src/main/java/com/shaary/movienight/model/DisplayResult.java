package com.shaary.movienight.model;

import java.util.List;

public class DisplayResult {
    private String name;
    private String poster_path;
    private String release_date;
    private String overview;
    private List<Integer> genre_ids;
    private int vote_count;
    private int id;
    private double vote_average;
    private double popularity;

    public DisplayResult(String name, String poster_path, String release_date, String overview,
                         List<Integer> genre_ids, int vote_count, int id, double vote_average, double popularity) {
        this.name = name;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.overview = overview;
        this.genre_ids = genre_ids;
        this.vote_count = vote_count;
        this.id = id;
        this.vote_average = vote_average;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
}
