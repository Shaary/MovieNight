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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

//This fragment takes bundle of args when the recyclerview is clicked at set them to it's view
public class ChoiceDialogFragment extends DialogFragment {

    public static final String TAG = ChoiceDialogFragment.class.getSimpleName();

    @BindView(R.id.summary_text) TextView summary;
    @BindView(R.id.choice_ok_button) Button okButton;
    @BindView(R.id.choice_ratingBar) RatingBar ratingBar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.num_of_votes) TextView numOfVotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choice_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        summary.setMovementMethod(new ScrollingMovementMethod());

        numOfVotes.setText(String.format(Locale.getDefault(), "(Voted: %d)", getArguments().getInt("vote count")));
        ratingBar.setRating(getArguments().getFloat("rating"));
        summary.setText(getArguments().getString("overview"));
        title.setText(getArguments().getString("title"));

        okButton.setOnClickListener(v -> getDialog().dismiss());

        return view;
    }
}
