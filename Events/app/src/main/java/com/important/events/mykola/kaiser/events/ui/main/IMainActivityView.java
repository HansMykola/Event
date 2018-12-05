package com.important.events.mykola.kaiser.events.ui.main;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpView;
import com.important.events.mykola.kaiser.events.model.Event;

import java.util.ArrayList;

public interface IMainActivityView extends MvpView
{
    void addNewFragments(ArrayList<MvpAppCompatFragment> fragments);
    void startWork();
    void turnOffPage();
    void visibleDisplay();
    void startReadActivity(Event event, int index, boolean isOwner);
    void signOut();
}
