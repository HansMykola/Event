package com.important.events.mykola.kaiser.events.ui.search;

import com.arellomobile.mvp.MvpView;
import com.important.events.mykola.kaiser.events.model.Event;

import java.util.ArrayList;

public interface ISearchFragmentView extends MvpView
{
    void updateRecyclerViewAdapter();
    void changeAdapter(ArrayList<Event> events);
}
