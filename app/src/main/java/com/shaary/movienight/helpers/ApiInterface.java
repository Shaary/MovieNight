package com.shaary.movienight.helpers;

import com.shaary.movienight.model.DataResults;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/discover/movie")
    Call<DataResults> getListOfMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("sort_by") String sortBy,
            @Query("page") int page,
            @Query("vote_count.gte") int voteCount,
            @Query("vote_average.gte") float voteAverage,
            @Query("year") int year,
            @Query("with_genres") String genreId,
            @Query("primary_release_date.gte") String beginDate,
            @Query("primary_release_date.lte") String endDate
    );

    @GET("/3/discover/tv")
    Call<DataResults> getListOfTvShows(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("sort_by") String sortBy,
            @Query("page") int page,
            @Query("vote_count.gte") int voteCount,
            @Query("vote_average.gte") float voteAverage,
            @Query("year") int year,
            @Query("with_genres") String genreId,
            @Query("first_air_date.gte") String beginDate,
            @Query("first_air_date.lte") String endDate
    );
}
