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
import android.widget.TextView;

import com.shaary.movienight.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortTvShowDialog extends DialogFragment{

    public static final String TAG = SortTvShowDialog.class.getSimpleName();

    @BindView(R.id.sort_ok_button) Button okButton;
    @BindView(R.id.sort_dismiss_button)Button dismissButton;
    @BindView(R.id.sort_clear_all_button) Button clearAllButton;
    @BindView(R.id.type_of_sort_group) RadioGroup typeRadioGroup;
    @BindView(R.id.order_group) RadioGroup orderRadioGroup;
    @BindView(R.id.sort_text) TextView title;

    private String sortOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.sort_dialog_tv, container, false);
        ButterKnife.bind(this, view);

        //Sets dialog name to tv shows
        if(!MainActivity.isMovie && MainActivity.isTV) {
            title.setText(R.string.sort_tv_shows_text);
        }
        //Sets dialog name to movies and tv shows
        else if((MainActivity.isMovie && MainActivity.isTV)) {
            title.setText(R.string.sort_movies_and_tv_shows_text);
        }

        //Sets type of sort in main activity for api call
        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = typeRadioGroup.getCheckedRadioButtonId();

            switch (selectedId){
                case R.id.sort_by_popularity:
                    MainActivity.SORT_BY = "popularity";
                    break;
                case R.id.sort_by_release_date:
                    MainActivity.SORT_BY = "first_air_date";
                    break;
                case R.id.sort_by_average_vote:
                    MainActivity.SORT_BY = "vote_average";
                    break;
            }
        });

        orderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = orderRadioGroup.getCheckedRadioButtonId();

            switch (selectedId) {
                case R.id.asc_order_button:
                    sortOrder = ".asc";
                    Log.d(TAG, "onCheckedChanged: " + MainActivity.SORT_BY);
                    break;

                case R.id.desc_order_button:
                    sortOrder = ".desc";
                    Log.d(TAG, "onCheckedChanged: " + MainActivity.SORT_BY);
                    break;
            }
        });

        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        clearAllButton.setOnClickListener(v -> {
            MainActivity.SORT_BY = "popularity.desc";
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });

        //Checks if user chose sort order otherwise the program won't find the right shows on the continuous requests
        okButton.setOnClickListener(v -> {
            if (sortOrder != null) {
                MainActivity.SORT_BY += sortOrder;
            } else {
                MainActivity.SORT_BY += ".desc";
            }
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });

        return view;
    }
}
