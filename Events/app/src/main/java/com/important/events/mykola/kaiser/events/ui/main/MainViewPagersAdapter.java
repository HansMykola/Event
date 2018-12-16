package com.important.events.mykola.kaiser.events.ui.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.arellomobile.mvp.MvpAppCompatFragment;

import java.util.ArrayList;

public class MainViewPagersAdapter extends FragmentStatePagerAdapter {
    private ArrayList<MvpAppCompatFragment> mFragments;

    MainViewPagersAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    void addFragments(ArrayList<MvpAppCompatFragment> fragments) {
        mFragments.clear();
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    @Override
    public MvpAppCompatFragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
