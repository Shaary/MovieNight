package com.shaary.movienight.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.shaary.movienight.R;
import com.shaary.movienight.model.Genres;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;
import static com.shaary.movienight.ui.MainActivity.isBoth;
import static com.shaary.movienight.ui.MainActivity.isMovie;
import static com.shaary.movienight.ui.MainActivity.isTV;

public class GenreAlertDialog extends DialogFragment {

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<String> items = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (isMovie) {
            listItems = getResources().getStringArray(R.array.movie_genres);
        } else if (isTV || isBoth) {
            listItems = getResources().getStringArray(R.array.tv_show_genres);
        }

        checkedItems = new boolean[listItems.length];

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle("Choose your genre")
                .setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });
        //builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < mUserItems.size(); i++) {
                    items.add(listItems[mUserItems.get(i)]);
                    Log.d(TAG, "onClick: lol" + items);
                }
                getGenres();
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
            }
        });

        builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                }
                ((MainActivity)getActivity()).genreId = null;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    //requests genre's ids from Genre class and sets genreId in MainActivity
    public void getGenres() {

        Genres genres = new Genres();
        ((MainActivity)getActivity()).genreId = genres.getId(items);
    }
}
