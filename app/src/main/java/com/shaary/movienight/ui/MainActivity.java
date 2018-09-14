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

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    //Requst constants
    public static final String API_KEY = "f2388368dc53b8b5a5a298ec53148eed";
    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static int PAGE = 1;
    public static final String LANGUAGE = "en-US";
    public static final String IMAGE_SIZE = "w185";
    public static String append = "tv";

    //Request vars
    public static int voteCount = 0;
    public static float voteAverage = 0;
    public static int year = 0;
    public static String genreId = null;
    public static String releaseDateBegin = null;
    public static String releaseDateEnd = null;
    public static String SORT_BY = "popularity.desc";

    //Flags
    public static boolean isMovie = true;
    public static boolean isTV = false;

    //Answer holders
    private List<Result> listOfData = new ArrayList<>();
    private List<Result> newListOfData = new ArrayList<>();

    private static final int NUM_COLUMNS = 2;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ProgressBar progressBar;
    private ActionBarDrawerToggle toggle;
    public static String movieImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //TODO: use butterknife

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        recyclerView.setLayoutManager(gridLayoutManager);
        progressBar = findViewById(R.id.progress_bar);


        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d(TAG, "onCreate: started.");

        getDataResultsWithInit();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplaying(recyclerView)) {
                    PAGE++;
                    getDataResultsWithInit(PAGE);
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

        Call<DataResults> call = getCall();

        call.enqueue(new Callback<DataResults>() {
            @Override
            public void onResponse(@NonNull Call<DataResults> call, @NonNull Response<DataResults> response) {
                if (response.isSuccessful()) {
                    DataResults results = response.body();
                    assert results != null;
                    listOfData = results.getResults();
                    Log.d(TAG, "onResponse: getDataResultsWithInit page " + PAGE);
                    progressBar.setVisibility(View.GONE);

                } else {
                    showErrors(response.code());
                }
                Log.i(TAG, "onResponse: lol" + movieImageUrl);

                //Initializing Recycle view
                runOnUiThread(() -> {
                    setView(listOfData);
                    initRecyclerView(listOfData);});
            }

            @Override
            public void onFailure(@NonNull Call<DataResults> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong :( Check your internet connection", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong: lol", t);
                t.printStackTrace();
            }
        });
    }

    //TODO: check if it's possible to have only one get data results method

    protected void getDataResultsWithInit(int page) {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface myInterface = retrofit.create(ApiInterface.class);

        Call<DataResults> call;

        if (isMovie) {
            call = myInterface.getListOfMovies(API_KEY, LANGUAGE, SORT_BY, page,
                    voteCount, voteAverage, year, genreId, releaseDateBegin, releaseDateEnd);

            Log.d(TAG, "getCall: append" + append);
        } else {
            call = myInterface.getListOfTvShows(API_KEY, LANGUAGE, SORT_BY, page,
                    voteCount, voteAverage, year, genreId, releaseDateBegin, releaseDateEnd);
        }

        call.enqueue(new Callback<DataResults>() {
            @Override
            public void onResponse(@NonNull Call<DataResults> call, @NonNull Response<DataResults> response) {
                if (response.isSuccessful()) {
                    DataResults results = response.body();
                    assert results != null;
                    newListOfData = results.getResults();
                    Log.d(TAG, "onResponse: getDataResultsWithInit page " + page);
                    progressBar.setVisibility(View.GONE);

                } else {
                    showErrors(response.code());
                }
                Log.i(TAG, "onResponse: lol" + movieImageUrl);

                setView(newListOfData);
                //add new list to the old to prevent outOfBound exception when click on a new film
                listOfData.addAll(newListOfData);
                recyclerView.post(() -> recyclerViewAdapter.addResults(getListOfPosters(newListOfData), getListOfTitles(newListOfData)));
            }

            @Override
            public void onFailure(@NonNull Call<DataResults> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong :( Check your internet connection", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong: lol", t);
                t.printStackTrace();
            }
        });
    }


    private Call<DataResults> getCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface myInterface = retrofit.create(ApiInterface.class);

        Call<DataResults> call;

        if (isMovie) {
            call = myInterface.getListOfMovies(API_KEY, LANGUAGE, SORT_BY, PAGE,
                    voteCount, voteAverage, year, genreId, releaseDateBegin, releaseDateEnd);

            Log.d(TAG, "getCall: append" + append);
        } else {
            call = myInterface.getListOfTvShows(API_KEY, LANGUAGE, SORT_BY, PAGE,
                    voteCount, voteAverage, year, genreId, releaseDateBegin, releaseDateEnd);
        }
        return call;
    }

    private void setView(List<Result> data) {
        for (int i = 0; i < data.size(); i++) {
            Result movie = data.get(i);
            try {
                String posterPath = movie.getPoster_path();
                //Gets the movie poster
                movieImageUrl = BASE_IMAGE_URL + IMAGE_SIZE + posterPath;
                movie.setPoster_path(movieImageUrl);

            } catch (NullPointerException npe) {
                AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
                alertDialogFragment.show(getFragmentManager(), "error");
                npe.printStackTrace();
            }

        }
    }

    private List<String> getListOfTitles(List<Result> listOfData) {
        ArrayList<String> titles = new ArrayList<>();
        for (Result result : listOfData) {
            if (isMovie) {
                titles.add(result.getTitle());
            }
            if (isTV) {
                titles.add(result.getName());
            }
        }
        return titles;
    }

    private List<String> getListOfPosters(List<Result> listOfData) {
            ArrayList<String> posters = new ArrayList<>();
            for (Result result : listOfData) {
                posters.add(result.getPoster_path());
            }
            return posters;

        }

    private void initRecyclerView(List<Result> listOfData) {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        recyclerViewAdapter = new RecyclerViewAdapter(this, getListOfPosters(listOfData), getListOfTitles(listOfData));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        recyclerViewAdapter.setListener(position -> {
            ChoiceDialogFragment choiceDialogFragment = new ChoiceDialogFragment();
            Bundle extras = new Bundle();
            extras.putString("overview", listOfData.get(position).getOverview());
            extras.putFloat("rating", (float)listOfData.get(position).getVote_average());
            extras.putInt("vote count", listOfData.get(position).getVote_count());

            if (isMovie) {
                extras.putString("title", listOfData.get(position).getTitle());
                extras.putString("date", listOfData.get(position).getRelease_date());
                extras.putString("type", "movie");

            } else {
                extras.putString("title", listOfData.get(position).getName());
                extras.putString("date", listOfData.get(position).getFirst_air_date());
                extras.putString("type", "tv show");
            }
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

        if (id == R.id.genre_test) {
            GenreAlertDialog genreAlertDialog = new GenreAlertDialog();
            genreAlertDialog.show(getFragmentManager(), "genres");

            Log.d(TAG, "onOptionsItemSelected: lol" + genreId);
            return true;
        }

        if (id == R.id.vote_average) {
            RatingAlertDialog ratingAlertDialog = new RatingAlertDialog();
            ratingAlertDialog.show(getFragmentManager(), "votes");
            return true;
        }

        if (id == R.id.min_num_rating) {
            MinNumRatingDialog minNumRatingDialog = new MinNumRatingDialog();
            minNumRatingDialog.show(getFragmentManager(), "rating");
            return true;
        }

        if (id == R.id.release_range) {
            DateRangeDialog dateRangeDialog = new DateRangeDialog();
            dateRangeDialog.show(getFragmentManager(), "date");
            return true;
        }

        if (id == R.id.sort) {

            if (isMovie && !isTV) {
                SortMovieDialog sortDialog = new SortMovieDialog();
                sortDialog.show(getFragmentManager(), "sort");
            } else if ((!isMovie && isTV) || (isMovie && isTV)) {
                SortTvShowDialog sortDialog = new SortTvShowDialog();
                sortDialog.show(getFragmentManager(), "sort");
            }

//            Toast.makeText(this, "movies are sorted", Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "onNavigationItemSelected: old list, lol" + listOfData);
//            if (isMovie) {
//                final List<Result> newResults = DataResults.SORT_BY_TITLE(listOfData);
//                Log.i(TAG, "onNavigationItemSelected: new list, lol" + newResults);
//
//                //updates the adapter
//                adapter.updateResultsList(newResults);}
//
//            else {
//                final List<Result> newResults = DataResults.SORT_BY_NAME(listOfData);
//                Log.i(TAG, "onNavigationItemSelected: new list, lol" + newResults);
//                //updates the adapter
//                adapter.updateResultsList(newResults);
            return true;
        }

        if (id == R.id.clear_everything) {
            PAGE = 1;
            voteCount = 0;
            voteAverage = 0;
            year = 0;
            isMovie = true;
            isTV = false;
            genreId = null;
            releaseDateBegin = null;
            releaseDateEnd = null;
            SORT_BY = "popularity.desc";
            getDataResultsWithInit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO: implements methods that will sort. Figure out DiffUtil
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.movies:
                isMovie = true;
                isTV = false;
                resetPage();
                getDataResultsWithInit();
                Toast.makeText(this, "movies are selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.tv:
                isMovie = false;
                isTV = true;
                resetPage();
                getDataResultsWithInit();
                Toast.makeText(this, "tv shows are selected", Toast.LENGTH_SHORT).show();
                return true;

//            case R.id.movie_and_tv:
//                isMovie = true;
//                isTV = true;
//                resetPage();
//                Toast.makeText(this, "movies and tv shows are selected", Toast.LENGTH_SHORT).show();
//                return true;

        }
        return false;
    }

    public void resetPage() {
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



