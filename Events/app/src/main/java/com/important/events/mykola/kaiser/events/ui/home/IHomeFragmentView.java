package com.important.events.mykola.kaiser.events.ui.home;

import com.arellomobile.mvp.MvpView;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.interface_model.IReadAction;

import java.util.ArrayList;

public interface IHomeFragmentView extends MvpView
{
    void connectMinActivity(IReadAction iReadAction);
    void updateHomeAdapter();
    void updateProfile(String uri, String name);
    void changeRecyclerView(ArrayList<Event> events);
}
