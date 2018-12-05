package com.important.events.mykola.kaiser.events.ui.map;

import com.arellomobile.mvp.MvpView;

public interface IMapActivityView extends MvpView
{
    void init();
    void initMap();
    void chooseAddress(String address);
    void givePermissions(String[] permissions, int requestCode);
}
