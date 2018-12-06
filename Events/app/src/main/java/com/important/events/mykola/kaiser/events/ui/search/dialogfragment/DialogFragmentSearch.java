package com.important.events.mykola.kaiser.events.ui.search.dialogfragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.interface_model.IDialogSearch;

import java.util.Calendar;

public class DialogFragmentSearch extends DialogFragment
{
    public static final String TAG = "DialogFragmentSearch";

    private DatePickerDialog.OnDateSetListener mDateSetlistener;

    private EditText mEditName, mEditPrice;
    private Button mButtonDate, mButtonSearch;

    private IDialogSearch mIDialogSearch;

    private String mDate;



    public DialogFragmentSearch()
    { }

    @SuppressLint("ValidFragment")
    public DialogFragmentSearch(IDialogSearch iDialogSearch)
    {
        mIDialogSearch = iDialogSearch;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.search_dialog_setting_search, container, false);

        mDate = "";

        mEditName = view.findViewById(R.id.edit_search_by_name);
        mEditPrice = view.findViewById(R.id.edit_search_by_price);

        mButtonDate = view.findViewById(R.id.button_search_by_date);
        mButtonDate.setOnClickListener(v -> createDatePickerDialog());
        mButtonSearch = view.findViewById(R.id.button_search_by_params);

        mButtonSearch.setOnClickListener(v -> {
            mIDialogSearch.sendParams(mEditName.getText().toString(),
                    mEditPrice.getText().toString(),
                    mDate);
            getDialog().dismiss();
        });

        return view;
    }

    private void createDatePickerDialog()
    {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(MyApp.get(),
                R.style.Theme_AppCompat_DayNight_DarkActionBar,
                mDateSetlistener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();

        mDateSetlistener = (view, year, month, dayOfMonth) -> {
            mDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            Toast.makeText(getContext(), mDate, Toast.LENGTH_LONG).show();
        };
    }
}
