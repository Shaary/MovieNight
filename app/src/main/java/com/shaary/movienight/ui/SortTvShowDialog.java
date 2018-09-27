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
    @BindView(R.id.sort_by_popularity) RadioButton popularity;
    @BindView(R.id.sort_by_average_vote) RadioButton voteAverage;
    @BindView(R.id.sort_by_release_date) RadioButton releaseDate;

    @BindView(R.id.order_group) RadioGroup orderRadioGroup;
    @BindView(R.id.asc_order_button) RadioButton ascOrder;
    @BindView(R.id.desc_order_button) RadioButton descOrder;

    @BindView(R.id.sort_text) TextView title;

    private String sortOrder;
    private String sortBy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.sort_dialog_tv, container, false);
        ButterKnife.bind(this, view);

        //Sets the sort buttons checked
        checkTypeButtons(MainActivity.sortByType);
        checkOrderButtons(MainActivity.sortByOrder);

        //Sets dialog name to movies and tv shows
        if((MainActivity.isBoth)) {
            title.setText(R.string.sort_movies_and_tv_shows_text);
        } //Sets dialog name to tv shows
        else if(MainActivity.isTV) {
            title.setText(R.string.sort_tv_shows_text);
        }

        //Sets type of sort in main activity for api call
        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = typeRadioGroup.getCheckedRadioButtonId();
            Log.d(TAG, "onCreateView: selectedId " + selectedId);

            switch (selectedId){
                case R.id.sort_by_popularity:
                    sortBy = "popularity";
                    break;
                case R.id.sort_by_release_date:
                    sortBy = "first_air_date";
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
