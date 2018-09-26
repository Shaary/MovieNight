package com.shaary.movienight.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shaary.movienight.R;
import com.shaary.movienight.helpers.ApiInterface;
import com.shaary.movienight.helpers.RecyclerViewAdapter;
import com.shaary.movienight.model.DataResults;
import com.shaary.movienight.model.children.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //Request constants
    public static final String API_KEY = "f2388368dc53b8b5a5a298ec53148eed";
    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String LANGUAGE = "en-US";
    public static final String IMAGE_SIZE = "w185";
    public static int PAGE = 1;

    //Request vars
    public static float voteAverage = 0;
    public static int voteCount = 0;
    public static int year = 0;
    public static String genreId = null;
    public static String releaseDateBegin = null;
    public static String releaseDateEnd = null;
    public static String SORT_BY = "popularity.desc";
    public static String resultPosterPath;

    //Flags
    public static boolean isBoth = false;
    public static boolean isMovie = true;
    public static boolean isTV = false;
    //Checks if the next call can be made
    private boolean canLoad = false;

    //Answer holders
    private List<Result> listOfData = new ArrayList<>();
    private List<Result> newListOfData = new ArrayList<>();

    //UI vars
    private static final int NUM_COLUMNS = 2;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //Shows navigation icon
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        recyclerView.setLayoutManager(gridLayoutManager);

        navigationView.setNavigationItemSelectedListener(this);

        getDataResultsWithInit();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplaying(recyclerView)) {
                    PAGE++;
                    Log.d(TAG, "onScrolled: canDownload " + canLoad);
                    if (isBoth) {
                        if (!canLoad) {
                            isMovie = true;
                            isTV = false;
                            canLoad = true;
                            getDataResultsWithInit();
                        } else {
                            isMovie = false;
                            isTV = true;
                            getDataResultsWithInit();
                            canLoad = false;
                        }
                    } else {
                        getDataResultsWithInit();
                    }
                }
            }
        });
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    protected void getDataResultsWithInit() {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface myInterface = retrofit.create(ApiInterface.class);

        Call<DataResults> call;

        if (isMovie) {
            call = myInterface.getListOfMovies(API_KEY, LANGUAGE, SORT_BY, PAGE,
                    voteCount, voteAverage, year, genreId, releaseDateBegin, releaseDateEnd);
        } else {
            call = myInterface.getListOfTvShows(API_KEY, LANGUAGE, SORT_BY, PAGE,
                    voteCount, voteAverage, year, genreId, releaseDateBegin, releaseDateEnd);
        }

        call.enqueue(new Callback<DataResults>() {
            @Override
            public void onResponse(@NonNull Call<DataResults> call, @NonNull Response<DataResults> response) {
                if (response.isSuccessful()) {
                    DataResults results = response.body();
                    if (results.getResults().size() == 0){
                        Toast.makeText(MainActivity.this, R.string.toast_no_match_found_text, Toast.LENGTH_SHORT).show();
                    }
                    else if (listOfData.isEmpty()){
                        listOfData = results.getResults();
                        Log.d(TAG, "onResponse: getDataResultsWithInit page " + PAGE);
                        progressBar.setVisibility(View.GONE);

                        //Creates lists with info for recyclerview
                        ArrayList<String> titles = new ArrayList<>();
                        ArrayList<String> releaseDates = new ArrayList<>();
                        ArrayList<String> types = new ArrayList<>();
                        for (Result result : listOfData) {
                            if (isMovie) {
                                titles.add(result.getTitle());
                                releaseDates.add(result.getRelease_date());
                                types.add("Movie");
                            }
                            if (isTV) {
                                titles.add(result.getName());
                                releaseDates.add(result.getFirst_air_date());
                                types.add("Tv Show");
                            }
                        }

                        //Initializing Recycle view
                        runOnUiThread(() -> {
                            setPostersPath(listOfData);
                            initRecyclerView(listOfData, titles, releaseDates, types);});
                    } else {
                        newListOfData = results.getResults();
                        setPostersPath(newListOfData);

                        //Creates new lists with info to update recyclerview
                        ArrayList<String> newTitles = new ArrayList<>();
                        ArrayList<String> newReleaseDates = new ArrayList<>();
                        ArrayList<String> newTypes = new ArrayList<>();

                        for (Result result : newListOfData) {
                            if (isMovie) {
                                newTitles.add(result.getTitle());
                                newReleaseDates.add(result.getRelease_date());
                                newTypes.add("Movie");
                            }
                            if (isTV) {
                                newTitles.add(result.getName());
                                newReleaseDates.add(result.getFirst_air_date());
                                newTypes.add("Tv Show");
                            }
                        }
                        //Update recyclerview
                        recyclerView.post(() -> recyclerViewAdapter.addResults(getListOfPosters(newListOfData), newTitles, newReleaseDates, newTypes, newListOfData));
                    }
                } else {
                    showErrors(response.code());
                }
                progressBar.setVisibility(View.GONE);
                //Log.i(TAG, "onResponse: " + resultPosterPath);
            }

            @Override
            public void onFailure(@NonNull Call<DataResults> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.toast_something_went_wrong_text, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong: ", t);
                t.printStackTrace();
            }
        });
    }

    private void setPostersPath(List<Result> data) {
        for (int i = 0; i < data.size(); i++) {
            Result result = data.get(i);
            try {
                String posterPath = result.getPoster_path();
                //Gets the movie poster
                resultPosterPath = BASE_IMAGE_URL + IMAGE_SIZE + posterPath;
                result.setPoster_path(resultPosterPath);
            } catch (NullPointerException npe) {
                AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
                alertDialogFragment.show(getFragmentManager(), "error");
                npe.printStackTrace();
            }
        }
    }

    private List<String> getListOfPosters(List<Result> listOfData) {
            ArrayList<String> posters = new ArrayList<>();
            for (Result result : listOfData) {
                posters.add(result.getPoster_path());
            }
            return posters;
        }

    private void initRecyclerView(List<Result> listOfData, ArrayList<String> titles, ArrayList<String> releaseDates, ArrayList<String> types) {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        recyclerViewAdapter = new RecyclerViewAdapter(this, getListOfPosters(listOfData), titles, releaseDates, types, listOfData);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        //Shows movie or tv full info
        recyclerViewAdapter.setListener(position -> {
            ChoiceDialogFragment choiceDialogFragment = new ChoiceDialogFragment();
            Bundle extras = new Bundle();
            extras.putString("overview", listOfData.get(position).getOverview());
            extras.putFloat("rating", (float)listOfData.get(position).getVote_average());
            extras.putInt("vote count", listOfData.get(position).getVote_count());
            extras.putString("title", titles.get(position));

            choiceDialogFragment.setArguments(extras);
            choiceDialogFragment.show(getSupportFragmentManager(), "choice");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch (id) {
            case R.id.genre_test:
                GenreAlertDialog genreAlertDialog = new GenreAlertDialog();
                genreAlertDialog.show(getFragmentManager(), "genres");
                Log.d(TAG, "onOptionsItemSelected: " + genreId);
                return true;
            case R.id.vote_average:
                RatingAlertDialog ratingAlertDialog = new RatingAlertDialog();
                ratingAlertDialog.show(getFragmentManager(), "votes");
                return true;
            case R.id.min_num_rating:
                MinNumRatingDialog minNumRatingDialog = new MinNumRatingDialog();
                minNumRatingDialog.show(getFragmentManager(), "rating");
                return true;
            case R.id.release_range:
                DateRangeDialog dateRangeDialog = new DateRangeDialog();
                dateRangeDialog.show(getFragmentManager(), "date");
                return true;
            case R.id.sort:
                if (isTV || isBoth) {
                    SortTvShowDialog sortDialog = new SortTvShowDialog();
                    sortDialog.show(getFragmentManager(), "sort");
                } else {
                    SortMovieDialog sortDialog = new SortMovieDialog();
                    sortDialog.show(getFragmentManager(), "sort");
                }
                return true;
            case R.id.clear_everything:
                resetPage();
                voteCount = 0;
                voteAverage = 0;
                year = 0;
                genreId = null;
                releaseDateBegin = null;
                releaseDateEnd = null;
                SORT_BY = "popularity.desc";
                getDataResultsWithInit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//        if (id == R.id.genre_test) {
//            GenreAlertDialog genreAlertDialog = new GenreAlertDialog();
//            genreAlertDialog.show(getFragmentManager(), "genres");
//
//            Log.d(TAG, "onOptionsItemSelected: " + genreId);
//            return true;
//        }
//
//        if (id == R.id.vote_average) {
//            RatingAlertDialog ratingAlertDialog = new RatingAlertDialog();
//            ratingAlertDialog.show(getFragmentManager(), "votes");
//            return true;
//        }
//
//        if (id == R.id.min_num_rating) {
//            MinNumRatingDialog minNumRatingDialog = new MinNumRatingDialog();
//            minNumRatingDialog.show(getFragmentManager(), "rating");
//            return true;
//        }
//
//        if (id == R.id.release_range) {
//            DateRangeDialog dateRangeDialog = new DateRangeDialog();
//            dateRangeDialog.show(getFragmentManager(), "date");
//            return true;
//        }
//
//        if (id == R.id.sort) {
//            //When you call both movies and tv shows it shows you sort options for tv shows only
//            //because otherwise sort by revenue which tv shows don't have will affect the app performance
//            if (isTV || isBoth) {
//                SortTvShowDialog sortDialog = new SortTvShowDialog();
//                sortDialog.show(getFragmentManager(), "sort");
//            } else {
//                SortMovieDialog sortDialog = new SortMovieDialog();
//                sortDialog.show(getFragmentManager(), "sort");
//            }
//
////            Toast.makeText(this, "movies are sorted", Toast.LENGTH_SHORT).show();
////            Log.i(TAG, "onNavigationItemSelected: old list, lol" + listOfData);
////            if (isMovie) {
////                final List<Result> newResults = DataResults.SORT_BY_TITLE(listOfData);
////                Log.i(TAG, "onNavigationItemSelected: new list, lol" + newResults);
////
////                //updates the adapter
////                adapter.updateResultsList(newResults);}
////
////            else {
////                final List<Result> newResults = DataResults.SORT_BY_NAME(listOfData);
////                Log.i(TAG, "onNavigationItemSelected: new list, lol" + newResults);
////                //updates the adapter
////                adapter.updateResultsList(newResults);
//            return true;
//        }

//        if (id == R.id.clear_everything) {
//            resetPage();
//            voteCount = 0;
//            voteAverage = 0;
//            year = 0;
//            genreId = null;
//            releaseDateBegin = null;
//            releaseDateEnd = null;
//            SORT_BY = "popularity.desc";
//            getDataResultsWithInit();
//            return true;
//        }



    //TODO: implements methods that will sort. Figure out DiffUtil
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.movies:
                isMovie = true;
                isTV = false;
                isBoth = false;
                resetPage();
                getDataResultsWithInit();
                Toast.makeText(this, R.string.toast_movies_are_selected, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.tv:
                isMovie = false;
                isTV = true;
                isBoth = false;
                resetPage();
                getDataResultsWithInit();
                Toast.makeText(this, R.string.toast_tv_shows_are_selected, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.movie_and_tv:
                resetPage();
                isBoth = true;
                isMovie = false;
                isTV = true;
                getDataResultsWithInit();
                Toast.makeText(this, R.string.toast_mov_and_tv_are_selected, Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public void resetPage() {
        listOfData.clear();
        newListOfData.clear();
        PAGE = 1;
    }

    private void showErrors(int errorCode) {
        switch (errorCode) {
            case 404:
                Toast.makeText(MainActivity.this, "server returned error: page is not found",
                        Toast.LENGTH_SHORT).show();
                break;

            case 500:
                Toast.makeText(MainActivity.this, "server returned error: server is broken",
                        Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(MainActivity.this, "server returned error: unknown error",
                        Toast.LENGTH_SHORT).show();
        }
    }
}



