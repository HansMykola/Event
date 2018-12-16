package com.important.events.mykola.kaiser.events.ui.create;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.file.FileHelper;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectCreateFragment;
import com.important.events.mykola.kaiser.events.model.interface_model.IUpdateAdapter;
import com.important.events.mykola.kaiser.events.ui.map.MapActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class CreateFragment extends MvpAppCompatFragment implements ICreateFragmentView,
        View.OnClickListener {
    private final static int REQUEST_CODE_IMAGE = 14;
    public final static int REQUEST_CODE_PLACE = 88;

    private EditText mEditName, mEditPrice, mEditAllInfo;
    private Spinner mSpinnerCategory;
    private ImageView mImageEvent;
    DatePickerDialog mDatePiker;

    public CreateFragment() {
        if (mPresenter == null) {
            Toast.makeText(MyApp.get().getBaseContext(), "false", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MyApp.get().getBaseContext(), "true", Toast.LENGTH_LONG).show();
        }

    }

    private IUpdateAdapter updateAdapter;
    private IConnectCreateFragment mIConnectCreateFragment;

    @SuppressLint("ValidFragment")
    // TODO Bad design - fragments should NOT have non-empty constructors.
    public CreateFragment(IUpdateAdapter mUpdateAdapter, IConnectCreateFragment iConnectHomeFragment) {
        updateAdapter = mUpdateAdapter;
        mIConnectCreateFragment = iConnectHomeFragment;
    }

    @ProvidePresenter
    public CreateFragmentPresenter constructorPresenter() {
        return new CreateFragmentPresenter(updateAdapter, mIConnectCreateFragment);
    }

    @InjectPresenter
    public CreateFragmentPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_create_fragment, container, false);

        mEditName = view.findViewById(R.id.edit_name_event);
        mEditPrice = view.findViewById(R.id.edit_price);
        mEditAllInfo = view.findViewById(R.id.edit_all_info);

        mSpinnerCategory = view.findViewById(R.id.spinner_category);

        mPresenter.setCategory(mSpinnerCategory.getItemAtPosition(1).toString());

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setCategory(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mImageEvent = view.findViewById(R.id.image_event);
        mImageEvent.setOnClickListener(this);

        Button mButtonDate = view.findViewById(R.id.button_choose_date);
        mButtonDate.setOnClickListener(this);

        Button mButtonMap = view.findViewById(R.id.button_choose_place);
        mButtonMap.setOnClickListener(this);

        Button mButtonAdd = view.findViewById(R.id.button_add_create_event);
        mButtonAdd.setOnClickListener(this);

        mPresenter.connectFragment();

        return view;
    }

    // TODO this is bad practice, to catch all clicks in one method.
    // TODO Use different methods for each view instead.
    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.button_choose_date: {
                createDatePickerDialog();
                break;
            }
            case R.id.button_choose_place: {
                mIntent = new Intent("GoogleMapEvent");
                startActivityForResult(mIntent, REQUEST_CODE_PLACE);
                break;
            }
            case R.id.button_add_create_event: {
                if (mPresenter.canAddEvent(mEditName.getText().toString(),
                        mEditPrice.getText().toString(),
                        mEditAllInfo.getText().toString())) {
                    mImageEvent.buildDrawingCache();
                    mPresenter.setEventDate(mEditName.getText().toString(),
                            Double.valueOf(mEditPrice.getText().toString()),
                            mEditAllInfo.getText().toString(),
                            // TODO This is really bad.
                            // TODO You have image URI, so just get a file from that URI, and copy it.
                            // TODO Drawing cache isn't made for this.
                            // TODO Anyway, avoid using Bitmap in your code. It's really memory-eating thing.
                            mImageEvent.getDrawingCache());
                    mImageEvent.destroyDrawingCache();
                } else {
                    Toast.makeText(getContext(), "Ви не ввели всі дані", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.image_event: {
                mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(mIntent, REQUEST_CODE_IMAGE);
                break;
            }
        }
    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDatePiker = new DatePickerDialog(getContext(),
                R.style.Theme_Design_Light,
                (view, year1, month1, dayOfMonth) ->
                        mPresenter.setDate(dayOfMonth + "-" + (month + 1) + "-" + year),
                year, month, day);


        mDatePiker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mDatePiker.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_IMAGE: {
                if (data != null) {
                    mPresenter.setUri(data.getData());
                }
                break;
            }
            case REQUEST_CODE_PLACE: {
                if (data != null) {
                    mPresenter.setAddress(data.getStringExtra(MapActivity.KEY));
                    Toast.makeText(getContext(), mPresenter.getAddress(), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void openImage(Uri uri) {
        mImageEvent.setImageURI(uri);
    }

    @Override
    public void clearPage() {
        mEditName.setText("");
        mEditPrice.setText("");
        mEditAllInfo.setText("");
        mSpinnerCategory.setSelection(0);

        Resources resources = MyApp.get().getResources();
        mImageEvent.setImageDrawable(resources
                .getDrawable(R.drawable.question1, null));
    }

    @Override
    public void editEvent(Event event, FileHelper mFileHelper) {
        mEditName.setText(event.getName());
        mEditPrice.setText(String.valueOf(event.getPrice()));
        mEditAllInfo.setText(event.getDescription());
        Picasso.get().load(mFileHelper.readImage(event.getName())).into(mImageEvent);

        int index = 0;
        String[] array = getResources().getStringArray(R.array.category);
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(event.getCategory())) {
                index = i;
            }
        }

        mSpinnerCategory.setSelection(index);
        mPresenter.setmEvent(event);
    }

}