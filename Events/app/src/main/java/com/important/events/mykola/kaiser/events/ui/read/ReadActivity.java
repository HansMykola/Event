package com.important.events.mykola.kaiser.events.ui.read;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.file.FileHelper;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.User;
import com.squareup.picasso.Picasso;

public class ReadActivity extends MvpAppCompatActivity
{
    private TextView mTextName, mTextCategory, mTextPrice, mTextDate, mTextPlace, mTextAllInfo;
    private ImageView mImageEvent;
    private LinearLayout mLinearButton;

    private Button mButtonDelete, mButtonUpdate;

    public final static String READ = "ReadActivity";
    public final static String KEY_ACTION = "key read";
    public final static String KEY_INDEX = "index";

    public final static int READ_UPDATE = 1488;
    public static final int READ_DELETE = 181;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        mLinearButton = findViewById(R.id.linear_button);

        Event event = getIntent().getParcelableExtra(READ);

        mImageEvent = findViewById(R.id.image_read_event);
        Picasso.get().load((new FileHelper()).readImage(event.getName())).into(mImageEvent);

        mTextName = findViewById(R.id.text_name_event);
        mTextCategory = findViewById(R.id.text_category);
        mTextPrice = findViewById(R.id.text_price);
        mTextDate = findViewById(R.id.text_date);
        mTextPlace = findViewById(R.id.text_place);
        mTextAllInfo = findViewById(R.id.text_all_info);

        mTextName.setText(event.getName());
        mTextCategory.setText(event.getCategory());
        mTextPrice.setText("$ " + event.getPrice());
        mTextDate.setText(event.getDate());
        mTextPlace.setText(event.getLocation());
        mTextAllInfo.setText(event.getDescription());

        if (getIntent().getExtras().getBoolean(KEY_ACTION))
        {
            mLinearButton.setVisibility(View.VISIBLE);

            mButtonDelete = findViewById(R.id.button_delete);

            mButtonDelete.setOnClickListener(v -> {
                User user = MyApp.get().getUser();
                MyApp.get().getDatabaseEvent().deleteEvent(event);
                MyApp.get().getDatabaseEvent().deleteUserEvent(user);
                (new FileHelper()).deleteImage(event.getName());
                user.deleteEvents(user.getMyEvents(), event);
                setResult(READ_DELETE);
                finish();
            });

            mButtonUpdate = findViewById(R.id.button_update);
            mButtonUpdate.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra("ID", getIntent().getIntExtra(KEY_INDEX, 0));
                setResult(READ_UPDATE, intent);
                finish();
            });
        }
    }
}
