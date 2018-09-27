package com.shaary.movienight.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shaary.movienight.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortMovieDialog extends DialogFragment{

    public static final String TAG = SortMovieDialog.class.getSimpleName();

    @BindView(R.id.sort_ok_button) Button okButton;
    @BindView(R.id.sort_dismiss_button) Button dismissButton;
    @BindView(R.id.sort_clear_all_button) Button clearAllButton;

    @BindView(R.id.type_of_sort_group) RadioGroup typeRadioGroup;
    @BindView(R.id.sort_by_popularity) RadioButton popularity;
    @BindView(R.id.sort_by_average_vote) RadioButton voteAverage;
    @BindView(R.id.sort_by_number_of_votes) RadioButton numberOfVotes;
    @BindView(R.id.sort_by_release_date) RadioButton releaseDate;
    @BindView(R.id.sort_by_revenue) RadioButton revenue;

    @BindView(R.id.order_group) RadioGroup orderRadioGroup;
    @BindView(R.id.asc_order_button) RadioButton ascOrder;
    @BindView(R.id.desc_order_button) RadioButton descOrder;

    private String sortOrder;
    private String sortBy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.sort_dialog_movie, container, false);
        ButterKnife.bind(this, view);

        //Sets the sort buttons checked
        checkTypeButtons(MainActivity.sortByType);
        checkOrderButtons(MainActivity.sortByOrder);

        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = typeRadioGroup.getCheckedRadioButtonId();

            switch (selectedId){

                //Uses sortBy as separate variable to check in okButtonClick if user checked something
                case R.id.sort_by_popularity:
                    sortBy = "popularity";
                    break;

                case R.id.sort_by_release_date:
                    sortBy = "release_date";
                    break;

                case R.id.sort_by_revenue:
                    sortBy = "revenue";
                    break;

                case R.id.sort_by_number_of_votes:
                    sortBy = "vote_count";
                    break;

                case R.id.sort_by_average_vote:
                    sortBy = "vote_average";
                    break;
            }
            MainActivity.sortByType = sortBy;
        });

        orderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = orderRadioGroup.getCheckedRadioButtonId();

            switch (selectedId) {
                case R.id.asc_order_button:
                    sortOrder = ".asc";
                    break;

                case R.id.desc_order_button:
                    sortOrder = ".desc";
                    break;
            }
            MainActivity.sortByOrder = sortOrder;
        });

        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        clearAllButton.setOnClickListener(v -> {
            MainActivity.sortByType = "popularity";
            MainActivity.sortByOrder = ".desc";
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });

        //Checks if user chose sort order otherwise the program won't find the right shows on the continuous requests
        okButton.setOnClickListener(v -> {
            if (sortOrder != null || sortBy != null) {
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
            }
            getDialog().dismiss();
        });

        return view;
    }

    private void checkTypeButtons(String buttonName) {
        switch (buttonName) {
            case "popularity":
                typeRadioGroup.check(popularity.getId());
                break;

            case "release_date":
                typeRadioGroup.check(releaseDate.getId());
                break;

            case "revenue":
                typeRadioGroup.check(revenue.getId());
                break;

            case "vote_count":
                typeRadioGroup.check(numberOfVotes.getId());
                break;

            case "vote_average":
                typeRadioGroup.check(voteAverage.getId());
                break;
        }
    }

    private void checkOrderButtons(String order) {
        switch (order) {
            case ".asc":
                orderRadioGroup.check(ascOrder.getId());
                break;

            case ".desc":
                orderRadioGroup.check(descOrder.getId());
                break;
        }
    }
}
