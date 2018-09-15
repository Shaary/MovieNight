package com.shaary.movienight.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shaary.movienight.R;

public class ChoiceDialogFragment extends DialogFragment {

    public static final String TAG = ChoiceDialogFragment.class.getSimpleName();

    private TextView summary;
    private Button ok;
    private RatingBar ratingBar;
    private TextView title;
    private TextView numOfVotes;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choice_dialog_fragment, container, false);

        summary = view.findViewById(R.id.summary_text);
        ok = view.findViewById(R.id.choice_ok_button);
        ratingBar = view.findViewById(R.id.choice_ratingBar);
        title = view.findViewById(R.id.title);
        numOfVotes = view.findViewById(R.id.num_of_votes);

        summary.setMovementMethod(new ScrollingMovementMethod());

        numOfVotes.setText("(Voted: " + getArguments().getInt("vote count") + ")");
        ratingBar.setRating(getArguments().getFloat("rating"));
        summary.setText(getArguments().getString("overview"));
        title.setText(getArguments().getString("title"));
        Log.d(TAG, "onCreateView: lol" + getArguments().getFloat("rating"));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }
}
