package com.shaary.movienight.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.shaary.movienight.R;

public class SortMovieDialog extends DialogFragment{

    public static final String TAG = SortMovieDialog.class.getSimpleName();

    private Button okButton;
    private Button dismissButton;
    private Button clearAllButton;
    private RadioGroup typeRadioGroup;
    private RadioGroup orderRadioGroup;
    private String sortOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.sort_dialog_movie, container, false);

        okButton = view.findViewById(R.id.sort_ok_button);
        dismissButton = view.findViewById(R.id.sort_dismiss_button);
        clearAllButton = view.findViewById(R.id.sort_clear_all_button);

        typeRadioGroup = view.findViewById(R.id.type_of_sort_group);
        orderRadioGroup = view.findViewById(R.id.order_group);

        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId = typeRadioGroup.getCheckedRadioButtonId();

                switch (selectedId){

                    case R.id.sort_by_popularity:
                        MainActivity.SORT_BY = "popularity";
                        break;

                    case R.id.sort_by_release_date:
                        MainActivity.SORT_BY = "release_date";
                        break;

                    case R.id.sort_by_revenue:
                        MainActivity.SORT_BY = "revenue";
                        break;

                    case R.id.sort_by_number_of_votes:
                        MainActivity.SORT_BY = "vote_count";
                        break;

                    case R.id.sort_by_average_vote:
                        MainActivity.SORT_BY = "vote_average";
                        break;
                }
            }
        });

        orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId = orderRadioGroup.getCheckedRadioButtonId();

                switch (selectedId) {
                    case R.id.asc_order_button:
                        sortOrder= ".asc";
                        Log.d(TAG, "onCheckedChanged: lol" + MainActivity.SORT_BY);
                        break;

                    case R.id.desc_order_button:
                        sortOrder = ".desc";
                        Log.d(TAG, "onCheckedChanged: lol" + MainActivity.SORT_BY);
                        break;
                }
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
                MainActivity.SORT_BY = "popularity.desc";
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.SORT_BY += sortOrder;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });

        return view;
    }
}
