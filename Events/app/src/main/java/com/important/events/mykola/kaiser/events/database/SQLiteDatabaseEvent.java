package com.important.events.mykola.kaiser.events.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.User;

import java.util.ArrayList;

public class SQLiteDatabaseEvent extends SQLiteOpenHelper implements IConstUserTable,
                                                                        IConstUserEventTable,
                                                                        IConstEventTable
{
    private final static String DATABASE_NAME = "Events";
    private final static int VERSION = 1;

    public SQLiteDatabaseEvent(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String commandUser = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_NAME
                + " ( "
                + USER_KEY_ID + " TEXT, "
                + USER_KEY_NAME  + " TEXT, "
                + USER_KEY_URI  + " TEXT "
                + " ); ";
        db.execSQL(commandUser);
        //--------------------------------
        String  commandUserEvent = "CREATE TABLE IF NOT EXISTS " + TABLE_USEREVENT_NAME
                + " ( "
                + USEREVENT_KEY_IDUSER + " TEXT, "
                + USEREVENT_KEY_IDEVENT  + " INTEGER "
                + " ); ";
        db.execSQL(commandUserEvent);
        //--------------------------------
        String commandEvent = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT_NAME
                + " ( "
                + EVENT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EVENT_KEY_IDOWNER  + " TEXT, "
                + EVENT_KEY_NAME  + " TEXT, "
                + EVENT_KEY_URI  + " TEXT, "
                + EVENT_KEY_PRICE  + " REAL, "
                + EVENT_KEY_DATE  + " DATE, "
                + EVENT_KEY_LOCATION  + " TEXT, "
                + EVENT_KEY_CATEGORY  + " TEXT, "
                + EVENT_KEY_DESCRIPTION  + " TEXT "
                + " ); ";
        db.execSQL(commandEvent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    // Table User --------------------------------------------------------

    public void insertUser(User user)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_KEY_ID, user.getId());
        values.put(USER_KEY_NAME, user.getName());
        values.put(USER_KEY_URI, user.getUri());

        database.insert(TABLE_USER_NAME, null, values);
        database.close();
    }

    public void udateUser(User user)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(USER_KEY_NAME, user.getName());
        values.put(USER_KEY_URI, user.getUri());

        database.update(TABLE_USER_NAME,
                values,
                USER_KEY_ID + " =? ",
                new String[] { user.getId() });

        database.close();
    }

    public void deleteUser(User user)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_USER_NAME, USER_KEY_ID + " =? ", new String[] { user.getId() } );
        database.close();
    }

    public User getUser(String id)
    {
        SQLiteDatabase database = getReadableDatabase();
        User user = new User();

        user.setId(id);

        Cursor cursor = database.query(TABLE_USER_NAME,
                new String[]{ USER_KEY_NAME, USER_KEY_URI },
                USER_KEY_ID + " = ? ",
                new String[] {id},
                null,
                null,
                null);

        if(cursor.moveToFirst())
        {
            do
            {
                user.setName(cursor.getString(0));
                user.setUri(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        return user;
    }

    //Table UserEvent -------------------------------------------------

    public void insertUserEvent(User user, Event event)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USEREVENT_KEY_IDUSER, user.getId());
        values.put(USEREVENT_KEY_IDEVENT, event.getId());

        database.insert(TABLE_USEREVENT_NAME, null, values);
        database.close();
    }

    public void updateUserEvent(User user, Event event)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USEREVENT_KEY_IDUSER, user.getId());
        values.put(USEREVENT_KEY_IDEVENT, event.getId());

        database.update(TABLE_USEREVENT_NAME,
                values,
                USEREVENT_KEY_IDUSER + " =? ",
                new String[] { user.getId() });
        database.close();
    }

    public void deleteUserEvent(User user)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_USEREVENT_NAME, USEREVENT_KEY_IDUSER + " =? ", new String[] { user.getId() } );
        database.close();
    }

    public ArrayList<Integer> getListEventsId(String id)
    {
        ArrayList<Integer> listID = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_USEREVENT_NAME,
                new String[] { USEREVENT_KEY_IDEVENT },
                USEREVENT_KEY_IDUSER + "=?",
                new String[] {id},
                null,
                null,
                null);

        if (cursor.moveToFirst())
        {
            do
            {
                listID.add(cursor.getInt(0));
            }while (cursor.moveToNext());
        }
        return listID;
    }

    //Table Event -----------------------------------------------
    public void insertEvent(Event event)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EVENT_KEY_IDOWNER, event.getIdOwner());
        values.put(EVENT_KEY_NAME, event.getName());
        values.put(EVENT_KEY_URI, event.getUri());
        values.put(EVENT_KEY_PRICE, event.getPrice());
        values.put(EVENT_KEY_DATE, event.getDate());
        values.put(EVENT_KEY_LOCATION, event.getLocation());
        values.put(EVENT_KEY_CATEGORY, event.getCategory());
        values.put(EVENT_KEY_DESCRIPTION, event.getDescription());

        database.insert(TABLE_EVENT_NAME, null, values);
        database.close();
    }

    public void updateEvent(Event event)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EVENT_KEY_IDOWNER, event.getIdOwner());
        values.put(EVENT_KEY_NAME, event.getName());
        values.put(EVENT_KEY_URI, event.getUri());
        values.put(EVENT_KEY_PRICE, event.getPrice());
        values.put(EVENT_KEY_DATE, event.getDate());
        values.put(EVENT_KEY_LOCATION, event.getLocation());
        values.put(EVENT_KEY_CATEGORY, event.getCategory());
        values.put(EVENT_KEY_DESCRIPTION, event.getDescription());

        database.update(TABLE_EVENT_NAME,
                values,
                EVENT_KEY_ID + " =? ",
                new String[] { String.valueOf(event.getId()) });
        database.close();
    }

    public void deleteEvent(Event event)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_EVENT_NAME, EVENT_KEY_ID + " =? ", new String[] { String.valueOf(event.getId()) } );
        database.close();
    }

    public int getLastIdEvent()
    {
        SQLiteDatabase database = getReadableDatabase();

        String commands = "SELECT MAX(" + EVENT_KEY_ID +") FROM " + TABLE_EVENT_NAME;

        Cursor cursor = database.rawQuery(commands, null);

        if (cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return -1;
    }

    public ArrayList<Event> getEvents(String id, boolean forUser)
    {
        SQLiteDatabase database = getReadableDatabase();

        ArrayList<Event> events = new ArrayList<>();

        String[] commands = new String[2];

        if (forUser)
        {
            commands[0] = "SELECT DISTINCT A.* FROM " + TABLE_EVENT_NAME + " A, "
                    + TABLE_USEREVENT_NAME + " B "
                    + " WHERE A." + EVENT_KEY_ID + " = B." + USEREVENT_KEY_IDEVENT
                    + " AND B." + USEREVENT_KEY_IDUSER + " = ?  ";
            //commands[0] = "SELECT DISTINCT * FROM " + TABLE_USEREVENT_NAME + " WHERE " + USEREVENT_KEY_IDUSER + " = ? ";
            commands[1] = "SELECT DISTINCT * FROM " + TABLE_EVENT_NAME
                    + " WHERE  " + EVENT_KEY_IDOWNER + " = ?   ";
        }
        else
        {
            commands[0] = "SELECT DISTINCT A.* FROM " + TABLE_EVENT_NAME + " A, "
                    + TABLE_USEREVENT_NAME + " B "
                    + " WHERE A." + EVENT_KEY_ID + " = B." + USEREVENT_KEY_IDEVENT
                    + " AND B." + USEREVENT_KEY_IDUSER + " <> ?  ";
            commands[1] = "SELECT DISTINCT * FROM " + TABLE_EVENT_NAME
                    + " WHERE ( " + EVENT_KEY_IDOWNER + " <> ?  ) ";
        }

        for (int i = 0; i < commands.length; ++i)
        {
            Cursor cursor = database.rawQuery(commands[i], new String[] { id });

            if (cursor.moveToFirst())
            {
                do
                {
                    events.add(new Event(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getDouble(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8)
                    ));
                }while (cursor.moveToNext());
            }
        }

        return events;
    }

    public ArrayList<Event> getAllEvents(String selection, String[] selectionArgs)
    {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Event> events = new ArrayList<>();


        Cursor cursor = database.query(TABLE_EVENT_NAME,
                new String[] { EVENT_KEY_ID,
                        EVENT_KEY_IDOWNER,
                        EVENT_KEY_NAME,
                        EVENT_KEY_URI,
                        EVENT_KEY_PRICE,
                        EVENT_KEY_DATE,
                        EVENT_KEY_LOCATION,
                        EVENT_KEY_CATEGORY,
                        EVENT_KEY_DESCRIPTION},
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst())
        {
            do
            {
                events.add(new Event(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)
                ));
            }while (cursor.moveToNext());
        }

        return events;
    }
}