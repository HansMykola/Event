package com.important.events.mykola.kaiser.events.model.interface_model;

import android.graphics.Bitmap;

import com.important.events.mykola.kaiser.events.model.Event;

public interface IUpdateViewCreate {
    void updateViewCreate(Event event, boolean change);
    void clearWhenGoNextPage();
}
