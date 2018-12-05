package com.important.events.mykola.kaiser.events.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;

import java.io.IOException;
import java.util.List;

@InjectViewState
public class MapActivityPresenter extends MvpPresenter<IMapActivityView>
{
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final float DEFAULT_ZOOM = 15F;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;

    private Boolean mLocationPermissionsGranted;

    private String mAddressLocaton;

    public static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168),
            new LatLng(71, 136)
    );

    public MapActivityPresenter()
    {
        mLocationPermissionsGranted = false;
        getLocationPermission();
    }

    public void close()
    {
        getViewState().chooseAddress(mAddressLocaton);
    }

    public void setAddress(String address)
    {
        mAddressLocaton = address;
    }

    private void getLocationPermission()
    {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(MyApp.get().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(MyApp.get().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionsGranted = true;
                getViewState().initMap();
            }
            else
            {
                getViewState().givePermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            getViewState().givePermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public String geoLocate(String searchString)
    {
        try
        {
            Geocoder geocoder = new Geocoder(MyApp.get()); // створюється об'єкт для визначення координать через геокодування
            List<Address> list = geocoder.getFromLocationName(searchString,  1);

            if (list.size() > 0)
            {
                Address address = list.get(0);

                moveCamera( new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));

                return address.getAddressLine(0);
            }
        }
        catch (Exception e)
        { }

        return "No";
    }

    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if (mLocationPermissionsGranted)
        {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(MyApp.get(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MyApp.get(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            getViewState().init();
        }
    }



    private void getDeviceLocation()
    {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MyApp.get());

        try
        {
            if (mLocationPermissionsGranted)
            {
                final Task location = mFusedLocationProviderClient.getLastLocation(); // остання позиція
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, MyApp.get().getString(R.string.here_you));
                        //getLatitude - Широта
                        //getLongitude - Довгота
                    }
                });
            }
        }
        catch (SecurityException e)
        {
        }
    }


    public void resultPermission(int[] grantResults, int requestCode)
    {
        mLocationPermissionsGranted = false;

        switch(requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length > 0)
                {
                    for(int i = 0; i < grantResults.length; i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    getViewState().initMap();
                }
            }
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("Тут ви"))
        {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }
}
