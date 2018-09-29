package com.shaary.movienight.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.shaary.movienight.R;
import com.shaary.movienight.model.Genres;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.constraint.Constraints.TAG;
import static com.shaary.movienight.ui.MainActivity.isBoth;
import static com.shaary.movienight.ui.MainActivity.isMovie;
import static com.shaary.movienight.ui.MainActivity.isTV;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener{

    String[] listOfGenres;
    //Saves ids of buttons that were checked
    ArrayList<Integer> chosenGenresIds = new ArrayList<>();
    //Used to uncheck buttons when refresh results or press clear all
    HashMap<Integer, String> genreSet = new HashMap<>();
    ArrayList<String> genres = new ArrayList<>();
    @BindView(R.id.genre_linear_layout) LinearLayout linearLayout;
    @BindView(R.id.genre_ok_button) Button okButton;
    @BindView(R.id.genre_dismiss_button) Button dismissButton;
    @BindView(R.id.genre_clear_all_button) Button clearAllButton;

    public GenreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.genre_fragment, container, false);
        ButterKnife.bind(this, view);


        Log.d(TAG, "onCreateView: in the beginning chosen ids " + chosenGenresIds.size());

        //Movie and TV has different genres. It checks which list to display
        if (isMovie) {
            listOfGenres = getResources().getStringArray(R.array.movie_genres);
        } else if (isTV || isBoth) {
            listOfGenres = getResources().getStringArray(R.array.tv_show_genres);
        }

        Context context = getActivity();

        //Creates a list of checkboxes
        for (int i = 0; i < listOfGenres.length; i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setId(i);
            checkBox.setText(listOfGenres[i]);
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.result_font));
            checkBox.setAllCaps(true);
            checkBox.setOnCheckedChangeListener(this);
            linearLayout.addView(checkBox);
        }

        //If activity saved some genre checks corresponding boxes
        if (MainActivity.genre != null) {
            chosenGenresIds = MainActivity.genreIds;
            for (int i : chosenGenresIds) {
                CheckBox box = (CheckBox) linearLayout.getChildAt(i);
                box.setChecked(true);
            }
        } else {
            //If activity is in default state unchecks previously checked boxes
            chosenGenresIds = MainActivity.genreIds;
            uncheckButtons();
            chosenGenresIds.clear();
        }

        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        clearAllButton.setOnClickListener(v -> {
            uncheckButtons();
            chosenGenresIds.clear();
            MainActivity.genre = null;
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
        });

        //Checks if user chose sort order otherwise the program won't find the right shows on the continuous requests
        okButton.setOnClickListener(v -> {
            getGenreNameById();
            String genreNames = getGenres();
            //Saves chosenGenresIds in MainActivity for future use
            MainActivity.genreIds = chosenGenresIds;
            MainActivity.genre = genreNames;
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();

            getDialog().dismiss();
        });
        return view;
    }

    private void uncheckButtons() {
        for (int i : chosenGenresIds) {
            CheckBox box = (CheckBox) linearLayout.getChildAt(i);
            box.setChecked(false);
        }
    }

    //In chosenGenresIds each value is equal to position of genre in listOfGenres
    //If chosenGenresIds.get(1) returns 6 then it corresponds to 6th element in genres list = Documentary
    private void getGenreNameById() {
        for (int i = 0; i < chosenGenresIds.size(); i++) {
            genres.add(listOfGenres[chosenGenresIds.get(i)]);
            Log.d(TAG, "onClick: " + genres);
        }
    }

    public String getGenres() {
        Genres genres = new Genres();
        return genres.getId(this.genres);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        String title = buttonView.getText().toString();
        Log.d(TAG, "onCheckedChanged: id " + id);
        Log.d(TAG, "onCheckedChanged: title " + title);
        if (isChecked) {
            if (!chosenGenresIds.contains(id)) {
                genreSet.put(id, title);
                chosenGenresIds.add(id);
            }
        } else {
           genreSet.remove(id);
        }
    }
}
