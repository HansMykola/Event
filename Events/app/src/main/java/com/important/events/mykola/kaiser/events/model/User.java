package com.important.events.mykola.kaiser.events.model;

import java.util.ArrayList;

public class User
{
    private String id;
    private String name;
    private String uri;
    private ArrayList<Event> subscriptions;
    private ArrayList<Event> myEvents;

    public User()
    {
        subscriptions = new ArrayList<>();
        myEvents = new ArrayList<>();
    }

    public User(String name, String uri)
    {
        this.name = name;
        this.uri = uri;
    }

    public boolean isInSubscriptions(int id)
    {
        for(Event event : subscriptions)
        {
            if (id == event.getId())
            {
                return true;
            }
        }
        return false;
    }

    public void searchMyEvent(ArrayList<Event> events)
    {
        for (Event event : events)
        {
            if (event.getIdOwner().equals(id))
            {
                myEvents.add(event);
            }
            else
            {
                subscriptions.add(event);
            }
        }
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public void setSubscriptions(ArrayList<Event> subscriptions)
    {
        this.subscriptions = subscriptions;
    }

    public void setASubscription(Event event)
    {
        subscriptions.add(event);
    }

    public void setMyEvents(ArrayList<Event> myEvents)
    {
        this.myEvents = myEvents;
    }

    public void setAMyEvent(Event event)
    {
        myEvents.add(event);
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getUri()
    {
        return uri;
    }

    public ArrayList<Event> getSubscriptions()
    {
        return subscriptions;
    }

    public ArrayList<Event> getMyEvents()
    {
        return myEvents;
    }

    public int lookForIndexEvent(Event event)
    {
        for(int i = 0; i < myEvents.size(); ++i)
        {
            if (event.getId() == myEvents.get(i).getId())
            {
                return i;
            }
        }
        return -1;
    }

    public void deleteEvents(ArrayList<Event> events, Event event)
    {
        for (int i = 0; i < events.size(); ++i)
        {
            if (events.get(i).getId() == event.getId())
            {
                events.remove(i);
                break;
            }
        }
    }
}