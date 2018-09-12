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


public class DateRangeDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = DateRangeDialog.class.getSimpleName();

    private Button okButton;
    private Button dismissButton;
    private Button clearAllButton;
    private TextView from;
    private TextView to;
    private DatePickerDialog.OnDateSetListener mOnBeginDateSetListener;
    private DatePickerDialog.OnDateSetListener mOnEndDateSetListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.date_range_dialog, container, false);

        okButton = view.findViewById(R.id.date_ok_button);
        dismissButton = view.findViewById(R.id.date_dismiss_button);
        clearAllButton = view.findViewById(R.id.date_clear_all_button);
        from = view.findViewById(R.id.date_begin);
        to = view.findViewById(R.id.date_end);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
              int year = calendar.get(Calendar.YEAR);
              int month = calendar.get(Calendar.MONTH);
              int day = calendar.get(Calendar.DAY_OF_MONTH);

              DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mOnBeginDateSetListener, year, month, day);
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              dialog.show();

            }
        });

        mOnBeginDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String beginDate = year + "-" + "0" +  month + "-" + dayOfMonth;
                Log.d(TAG, "onDateSet: lol" + beginDate);
                from.setText(beginDate);
                ((MainActivity)getActivity()).releaseDateBegin = beginDate;
            }
        };

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mOnEndDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mOnEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String endDate = year + "-" + "0" +  month + "-" + dayOfMonth;
                Log.d(TAG, "onDateSet: lol" + endDate);
                to.setText(endDate);
                ((MainActivity)getActivity()).releaseDateEnd = endDate;
            }
        };


        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).releaseDateEnd = null;
                ((MainActivity)getActivity()).releaseDateBegin = null;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity)getActivity()).releaseDateBegin = minNumVote;
//                ((MainActivity)getActivity()).releaseDateEnd = minNumVote;
                ((MainActivity)getActivity()).resetPage();
                ((MainActivity)getActivity()).getDataResultsWithInit();
                getDialog().dismiss();
            }
        });

        return view;
    }

    //have to implement it otherwise the class gives an error
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
