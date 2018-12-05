package com.important.events.mykola.kaiser.events.ui.create;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.file.FileHelper;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectCreateFragment;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectHomeFragment;
import com.important.events.mykola.kaiser.events.model.interface_model.IUpdateAdapter;
import com.important.events.mykola.kaiser.events.model.interface_model.IUpdateViewCreate;

@InjectViewState
public class CreateFragmentPresenter extends MvpPresenter<ICreateFragmentView> implements IUpdateViewCreate
{
    private String mFormerName;
    private Event mEvent;
    private Uri mUri;
    private boolean mUpdate;

    private IUpdateAdapter mUpdateAdapter;
    private IConnectCreateFragment mIConnectHomeFragment;

    public CreateFragmentPresenter(IUpdateAdapter update, IConnectCreateFragment iConnectHomeFragment)
    {
        Log.d("Home", "Create CreateFragmentPresenter");
        mUpdateAdapter = update;
        mUpdate = false;
        mEvent = new Event();
        mIConnectHomeFragment = iConnectHomeFragment;
    }

    public void connectFragment()
    {
        mIConnectHomeFragment.connectCreateFragment();
    }

    public void setEventDate(String name,
                             double price,
                             String description,
                             Bitmap bitmap)
    {
        Log.d("Home", "Create setEventDate");
        int index = MyApp.get().getUser().lookForIndexEvent(mEvent);

        mEvent.setIdOwner(MyApp.get().getUser().getId());
        mEvent.setName(name);

        mEvent.setPrice(price);
        mEvent.setDescription(description);

        FileHelper fileHelper = new FileHelper();

        if (!mUpdate)
        {
            fileHelper.saveImage(bitmap, mEvent.getName());

            String elem = mUri.toString();
            mEvent.setUri(elem);
            MyApp.get().getDatabaseEvent().insertEvent(mEvent);
            mEvent.setId(MyApp.get().getDatabaseEvent().getLastIdEvent());
            MyApp.get().getUser().setAMyEvent(mEvent);

            mEvent = new Event();
            mUri = null;
        }
        else
        {
            if (mUri != null)
            {
                String elem = mUri.toString();
                mEvent.setUri(elem);
            }
            fileHelper.deleteImage(mFormerName);
            fileHelper.saveImage(bitmap, mEvent.getName());

            MyApp.get().getUser().getMyEvents().set(index, mEvent);
            MyApp.get().getDatabaseEvent().updateEvent(mEvent);
        }

        getViewState().clearPage();
        mUpdateAdapter.updateAdapter();
    }

    public void setmEvent(Event mEvent)
    {
        Log.d("Home", "Create setmEvent");
        this.mEvent = mEvent;
    }

    public boolean canAddEvent(String name, String price, String description)
    {
        Log.d("Home", "Create canAddEvent");
        if ((mEvent.getDate() != null)
                && (mEvent.getLocation() != null)
                && (name != null)
                && (price !=null)
                && (description != null)
                )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setCategory(String category)
    {
        Log.d("Home", "Create setCategory");
        mEvent.setCategory(category);
    }

    public void setAddress(String address)
    {
        Log.d("Home", "Create setAddress");
        mEvent.setLocation(address);
    }

    public String getAddress()
    {
        Log.d("Home", "Create getAddress");
        return mEvent.getLocation();
    }

    public void setDate(String date)
    {
        Log.d("Home", "Create setDate");
        mEvent.setDate(date);
    }

    public void setUri(Uri uri)
    {
        Log.d("Home", "Create setUri");
        this.mUri = uri;
        getViewState().openImage(uri);
    }

    @Override
    public void updateViewCreate(Event event, boolean change) {
        Log.d("Home", "Create sendArguments");
        mEvent.setId(event.getId());
        mEvent.setIdOwner(event.getIdOwner());
        mEvent.setName(event.getName());
        mEvent.setPrice(event.getPrice());
        mEvent.setCategory(event.getCategory());
        mEvent.setLocation(event.getLocation());
        mEvent.setDescription(event.getDescription());
        mEvent.setDate(event.getDate());
        mEvent.setUri(mEvent.getUri());
        mFormerName = event.getName();
        getViewState().editEvent(mEvent, new FileHelper());

        mUpdate = change;
    }

    @Override
    public void clearWhenGoNextPage() {
        getViewState().clearPage();
    }
}
