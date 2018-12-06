package com.important.events.mykola.kaiser.events.ui.search;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.important.events.mykola.kaiser.events.database.SQLiteDatabaseEvent;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.model.interface_model.IChooseCategory;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectSearchFragment;
import com.important.events.mykola.kaiser.events.model.interface_model.IDialogSearch;
import com.important.events.mykola.kaiser.events.model.interface_model.IUpdateAdapter;
import com.important.events.mykola.kaiser.events.model.interface_model.IUpdateRecyclerViewSearch;

import java.util.ArrayList;

@InjectViewState
public class SearchFragmentPresenter extends MvpPresenter<ISearchFragmentView>
        implements IUpdateRecyclerViewSearch, IDialogSearch, IChooseCategory, IUpdateAdapter

{
    private SQLiteDatabaseEvent mDatabaseEvent;
    private ArrayList<Event> mEvents;
    private IConnectSearchFragment mIConnectSearchFragment;
    private boolean mCreateP;

    public SearchFragmentPresenter(IConnectSearchFragment iConnectSearchFragment)
    {
        mEvents = new ArrayList<>();
        mCreateP = false;
        mIConnectSearchFragment = iConnectSearchFragment;
    }

    public void connect()
    {
        if (!mCreateP)
        {
            mCreateP = true;
            mIConnectSearchFragment.connectSearchFragment();
        }
    }

    @Override
    public void sendParams(String name, String price, String date)
    {
        String select;
        String[] args;

        if ((name.trim().length() != 0) && (price.trim().length() != 0) && (date.trim().length() != 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_NAME + " =? AND "
                    + SQLiteDatabaseEvent.EVENT_KEY_PRICE + " =? AND "
                    + SQLiteDatabaseEvent.EVENT_KEY_DATE + " =? ";

            args = new String[]{ name, String.valueOf(price), date };
        }
        else if ((name.trim().length() != 0) && (price.trim().length() != 0) && (date.trim().length() == 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_NAME + " =? AND "
                    + SQLiteDatabaseEvent.EVENT_KEY_PRICE + " <= ?";

            args = new String[]{ name, String.valueOf(price) };
        }
        else if ((name.trim().length() != 0) && (price.trim().length() == 0) && (date.trim().length() != 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_NAME + " =? AND "
                    + SQLiteDatabaseEvent.EVENT_KEY_DATE + " =? ";

            args = new String[]{ name, date };
        }
        else if ((name.trim().length() == 0) && (price.trim().length() != 0) && (date.trim().length() != 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_PRICE + " <=? AND "
                    + SQLiteDatabaseEvent.EVENT_KEY_DATE + " =? ";

            args = new String[]{ String.valueOf(price), date };
        }
        else if ((name.trim().length() != 0) && (price.trim().length() == 0) && (date.trim().length() == 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_NAME + " =?";

            args = new String[]{ name };
        }
        else if ((name.trim().length() == 0) && (price.trim().length() != 0) && (date.trim().length() == 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_PRICE + " <=? ";

            args = new String[]{ String.valueOf(price) };
        }
        else if ((name.trim().length() == 0) && (price.trim().length() == 0) && (date.trim().length() != 0))
        {
            select = SQLiteDatabaseEvent.EVENT_KEY_DATE + " =? ";

            args = new String[]{ date };
        }
        else
        {
            return;
        }

        mEvents = mDatabaseEvent.getAllEvents(select, args);
        getViewState().changeAdapter(mEvents);
    }

    @Override
    public void updateRecyclerViewSearch() {
        mDatabaseEvent = MyApp.get().getDatabaseEvent();
        mEvents = mDatabaseEvent.getAllEvents(null, null);
        getViewState().changeAdapter(mEvents);
    }

    @Override
    public void chooseCategory(String name) {
        mEvents = mDatabaseEvent.getAllEvents(SQLiteDatabaseEvent.EVENT_KEY_CATEGORY + " =? ",
                new String[] { name });
        getViewState().changeAdapter(mEvents);
    }

    public void searchForName(String name)
    {
        mEvents = mDatabaseEvent.getAllEvents(SQLiteDatabaseEvent.EVENT_KEY_NAME + " =? ",
                new String[] { name });
        getViewState().changeAdapter(mEvents);
    }

    @Override
    public void updateAdapter() {
        getViewState().updateRecyclerViewAdapter();
        updateRecyclerViewSearch();
    }
}
