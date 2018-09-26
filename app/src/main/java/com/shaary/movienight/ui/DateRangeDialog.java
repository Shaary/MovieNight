package com.shaary.movienight.ui;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.shaary.movienight.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DateRangeDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = DateRangeDialog.class.getSimpleName();

    @BindView(R.id.date_ok_button) Button okButton;
    @BindView(R.id.date_dismiss_button) Button dismissButton;
    @BindView(R.id.date_clear_all_button) Button clearAllButton;
    @BindView(R.id.date_begin) TextView beginningDate;
    @BindView(R.id.date_end) TextView endDate;
    private DatePickerDialog.OnDateSetListener onBeginDateSetListener;
    private DatePickerDialog.OnDateSetListener onEndDateSetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.date_range_dialog, container, false);
        ButterKnife.bind(this, view);

        beginningDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
          int year = calendar.get(Calendar.YEAR);
          int month = calendar.get(Calendar.MONTH);
          int day = calendar.get(Calendar.DAY_OF_MONTH);

          //Sets date choosing dialog
          DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, onBeginDateSetListener, year, month, day);
          dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          dialog.show();
        });

        onBeginDateSetListener = (view12, year, month, dayOfMonth) -> {
            String beginDate = year + "-" + "0" +  month + "-" + dayOfMonth;
            Log.d(TAG, "onDateSet: lol" + beginDate);
            beginningDate.setText(beginDate);
            MainActivity.releaseDateBegin = beginDate;
        };

        endDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, onEndDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        onEndDateSetListener = (view1, year, month, dayOfMonth) -> {
            String endDate = year + "-" + "0" +  month + "-" + dayOfMonth;
            Log.d(TAG, "onDateSet: lol" + endDate);
            this.endDate.setText(endDate);
            MainActivity.releaseDateEnd = endDate;
        };

        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        clearAllButton.setOnClickListener(v -> {
            MainActivity.releaseDateEnd = null;
            MainActivity.releaseDateBegin = null;
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });

        okButton.setOnClickListener(v -> {
            ((MainActivity)getActivity()).resetPage();
            ((MainActivity)getActivity()).getDataResultsWithInit();
            getDialog().dismiss();
        });
        return view;
    }

    //have endDate implement it otherwise the class gives an error
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
