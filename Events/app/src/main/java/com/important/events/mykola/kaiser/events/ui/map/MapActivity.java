package com.important.events.mykola.kaiser.events.ui.map;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.ui.create.CreateFragment;
import com.important.events.mykola.kaiser.events.ui.map.addlibrary.PlaceAutocompleteAdapter;

import java.io.IOException;

public class MapActivity extends MvpAppCompatActivity implements IMapActivityView,
                                                              OnMapReadyCallback,
                                                              GoogleApiClient.OnConnectionFailedListener
{
    public final static String KEY_RESULT = "Map_Activity";
    public final static String KEY = "Address";

    private AutoCompleteTextView mSearchText;
    private ImageView mImageSearch;
    private Button mButtonChoose;

    @InjectPresenter
    public MapActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = findViewById(R.id.search_place);
        mImageSearch = findViewById(R.id.image_search);
        mButtonChoose = findViewById(R.id.button_choose);

        mButtonChoose.setOnClickListener(v -> {
            mPresenter.close();
        });

        mImageSearch.setOnClickListener(v -> {
            mPresenter.setAddress(mPresenter.geoLocate(mSearchText.getText().toString()));

        });
    }


    @Override
    public void init()
    {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        PlaceAutocompleteAdapter mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,
                mGoogleApiClient,
                MapActivityPresenter.LAT_LNG_BOUNDS,
                null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
    }

    @Override
    public void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void chooseAddress(String address)
    {
        if(!address.equals("No"))
        {
            Intent intent = getIntent();
            intent.putExtra(KEY, address);
            setResult(CreateFragment.REQUEST_CODE_PLACE, intent);
            finish();
        }
    }

    @Override
    public void givePermissions(String[] permissions, int requestCode)
    {
        ActivityCompat.requestPermissions(this,
                permissions,
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.resultPermission(grantResults, requestCode);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mPresenter.onMapReady(googleMap);
    }
}
