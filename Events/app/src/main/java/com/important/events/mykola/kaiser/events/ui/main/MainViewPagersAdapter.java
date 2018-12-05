package com.important.events.mykola.kaiser.events.ui.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.arellomobile.mvp.MvpAppCompatFragment;

import java.util.ArrayList;

public class MainViewPagersAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<MvpAppCompatFragment> mFragments;

    public MainViewPagersAdapter(FragmentManager fm)
    {
        super(fm);
        Log.d("Main", "MainViewPagersAdapter");
        mFragments = new ArrayList<>();
    }

    public void addFragments(ArrayList<MvpAppCompatFragment> fragments)
    {
        Log.d("Main", "addFragments");
        mFragments.clear();
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    @Override
    public MvpAppCompatFragment getItem(int i)
    {
        return mFragments.get(i);
    }

    @Override
    public int getCount()
    {
        return mFragments.size();
    }
}
