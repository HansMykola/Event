package com.important.events.mykola.kaiser.events.model.interface_model;

import com.important.events.mykola.kaiser.events.ui.home.HomeFragmentPresenter;

public interface IConnectHomeFragment
{
    void connectFragment(HomeFragmentPresenter homePresenter);
    void signOutProfile();
}
