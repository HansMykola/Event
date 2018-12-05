package com.important.events.mykola.kaiser.events.ui.create;

import android.graphics.Bitmap;
import android.net.Uri;

import com.arellomobile.mvp.MvpView;
import com.important.events.mykola.kaiser.events.file.FileHelper;
import com.important.events.mykola.kaiser.events.model.Event;

public interface ICreateFragmentView extends MvpView
{
    void clearPage();
    void openImage(Uri uri);
    void editEvent(Event event, FileHelper mFileHelper);
}
