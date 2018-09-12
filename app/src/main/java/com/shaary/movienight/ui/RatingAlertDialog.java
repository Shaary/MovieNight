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


public class RatingAlertDialog extends DialogFragment {

//    public interface OnInputListener{
//        void sendInput(String input);
//    }

    public static final String TAG = RatingAlertDialog.class.getSimpleName();
    //public OnInputListener onInputListener;

    private Button okButton;
    private Button dismissButton;
    private Button clearAllButton;
    private SeekBar seekBar;
    private float chosenRating;
    private TextView valueText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_alert_dialog, container, false);

        okButton = view.findViewById(R.id.ok_button);
        dismissButton = view.findViewById(R.id.dismiss_button);
        clearAllButton = view.findViewById(R.id.claer_all_button);
        valueText = view.findViewById(R.id.vote_value);

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chosenRating = (float)(progress / 10.0);
                valueText.setText(chosenRating + "");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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
                ((MainActivity)getActivity()).voteAverage = 0;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).voteAverage = chosenRating;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });


        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            onInputListener = (OnInputListener) getActivity();
//
//        } catch (ClassCastException cce) {
//            Log.e(TAG, "onAttach: ClassCastException" + cce.getMessage());
//        }
//    }
}
