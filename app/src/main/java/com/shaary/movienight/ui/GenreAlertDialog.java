package com.shaary.movienight.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.shaary.movienight.R;
import com.shaary.movienight.model.Genres;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;
import static com.shaary.movienight.ui.MainActivity.*;
import static com.shaary.movienight.ui.MainActivity.isBoth;
import static com.shaary.movienight.ui.MainActivity.isMovie;
import static com.shaary.movienight.ui.MainActivity.isTV;

public class GenreAlertDialog extends DialogFragment {

    String[] listOgGenres;
    boolean[] ifGenresChecked;
    ArrayList<Integer> chosenGenres = new ArrayList<>();
    ArrayList<String> genres = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Genres for movies and tv shows are different
        if (isMovie) {
            listOgGenres = getResources().getStringArray(R.array.movie_genres);
        } else if (isTV || isBoth) {
            listOgGenres = getResources().getStringArray(R.array.tv_show_genres);
        }
        ifGenresChecked = new boolean[listOgGenres.length];

        Context context = getActivity();

        //Sets up the dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle(R.string.genre_dialog_title)
                .setMultiChoiceItems(listOgGenres, ifGenresChecked, (dialogInterface, position, isChecked) -> {
                    if(isChecked){
                        chosenGenres.add(position);
                    }else{
                        chosenGenres.remove((Integer.valueOf(position)));
                    }
                });

        builder.setNegativeButton(R.string.dismiss_label, (dialogInterface, i) -> dialogInterface.dismiss());

        builder.setNeutralButton(R.string.clear_all_label, (dialogInterface, which) -> {
            for (int i = 0; i < ifGenresChecked.length; i++) {
                ifGenresChecked[i] = false;
                chosenGenres.clear();
            }
            genreId = null;
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
        });

        builder.setPositiveButton(R.string.ok_label, (dialogInterface, which) -> {
            for (int i = 0; i < chosenGenres.size(); i++) {
                genres.add(listOgGenres[chosenGenres.get(i)]);
                Log.d(TAG, "onClick: " + genres);
            }
            String genresIds = getGenres();
            if (genres.size() != 0) {
                genreId = genresIds;
                ((MainActivity) getActivity()).resetPage();
                ((MainActivity) getActivity()).getDataResultsWithInit();
            }
            dismiss();
        });

        return builder.create();
    }

    //requests genre's ids from Genre class and sets genreId in MainActivity
    public String getGenres() {
        Genres genres = new Genres();
        return genres.getId(this.genres);
    }
}
