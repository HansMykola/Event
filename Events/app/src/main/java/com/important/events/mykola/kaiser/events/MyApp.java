package com.important.events.mykola.kaiser.events;

import android.app.Application;

import com.important.events.mykola.kaiser.events.database.SQLiteDatabaseEvent;
import com.important.events.mykola.kaiser.events.model.User;

import java.util.ArrayList;

public class MyApp extends Application
{
    private static MyApp instance;

    private User user;
    private SQLiteDatabaseEvent databaseEvent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        user = new User();
        databaseEvent = new SQLiteDatabaseEvent(this);
    }

    public static MyApp get()
    {
        return instance;
    }

    public void clearUser()
    {
        user.setId(null);
        user.setName(null);
        user.setUri(null);
        user.setSubscriptions(new ArrayList<>());
        user.setMyEvents(new ArrayList<>());
    }

    public User getUser()
    {
        return user;
    }

    public SQLiteDatabaseEvent getDatabaseEvent()
    {
        return databaseEvent;
    }
}
