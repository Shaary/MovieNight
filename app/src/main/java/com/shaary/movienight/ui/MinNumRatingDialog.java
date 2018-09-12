package com.shaary.movienight.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.shaary.movienight.R;

public class MinNumRatingDialog extends DialogFragment{

    public static final String TAG = MinNumRatingDialog.class.getSimpleName();

    private Spinner spinner;
    private Button okButton;
    private Button dismissButton;
    private Button clearAllButton;
    private int minNumVote;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.min_num_rating_dialog, container, false);
        Context context = getActivity();

        spinner = view.findViewById(R.id.number_of_ratings);
        okButton = view.findViewById(R.id.num_rating_ok_button);
        dismissButton = view.findViewById(R.id.num_rating_dismiss);
        clearAllButton = view.findViewById(R.id.num_rating_clear_all);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.min_num_rating, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               minNumVote = Integer.parseInt(spinner.getItemAtPosition(position).toString());
                Log.d(TAG, "onItemSelected: lol " + minNumVote);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).voteCount = 0;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).voteCount = minNumVote;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });
        return view;
    }
}
