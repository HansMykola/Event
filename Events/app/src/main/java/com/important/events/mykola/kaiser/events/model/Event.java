package com.important.events.mykola.kaiser.events.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable
{
    private final static String TAG = "Event";

    private int id;
    private String idOwner;
    private String name;
    private double price;

    private String uri;
    private String date;
    private String category;
    private String location;
    private String description;

    public Event()
    {

    }

    public Event(int id, String idOwner, String name, String uri, double price, String date, String location, String category, String description)
    {
        this.id = id;
        this.idOwner = idOwner;
        this.name = name;
        this.price = price;
        this.uri = uri;
        this.date = date;
        this.category = category;
        this.location = location;
        this.description = description;
    }

    protected Event(Parcel in)
    {
        id = in.readInt();
        idOwner = in.readString();
        name = in.readString();
        price = in.readDouble();
        uri = in.readString();
        date = in.readString();
        category = in.readString();
        location = in.readString();
        description = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>()
    {
        @Override
        public Event createFromParcel(Parcel in)
        {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size)
        {
            return new Event[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(idOwner);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(uri);
        dest.writeString(date);
        dest.writeString(category);
        dest.writeString(location);
        dest.writeString(description);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setIdOwner(String idOwner)
    {
        this.idOwner = idOwner;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    public int getId()
    {
        return id;
    }

    public String getIdOwner()
    {
        return idOwner;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public String getUri()
    {
        return uri;
    }

    public String getDate()
    {
        return date;
    }

    public String getLocation()
    {
        return location;
    }

    public String getDescription()
    {
        return description;
    }
}
