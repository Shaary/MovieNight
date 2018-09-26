package com.shaary.movienight.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shaary.movienight.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RatingAlertDialog extends DialogFragment {

    public static final String TAG = RatingAlertDialog.class.getSimpleName();

   @BindView(R.id.ok_button) Button okButton;
   @BindView(R.id.dismiss_button) Button dismissButton;
   @BindView(R.id.claer_all_button) Button clearAllButton;
   @BindView(R.id.seekBar) SeekBar seekBar;
   @BindView(R.id.vote_value) TextView valueText;
    private float chosenRating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_alert_dialog, container, false);
        ButterKnife.bind(this, view);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chosenRating = (float)(progress / 10.0);
                valueText.setText(String.valueOf(chosenRating));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        clearAllButton.setOnClickListener(v -> {
            MainActivity.voteAverage = 0;
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });

        okButton.setOnClickListener(v -> {
            MainActivity.voteAverage = chosenRating;
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });
        return view;
    }
}
