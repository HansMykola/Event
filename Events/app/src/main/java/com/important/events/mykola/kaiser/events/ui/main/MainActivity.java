package com.important.events.mykola.kaiser.events.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.ui.main.dialogfragment.MainDialogFragment;
import com.important.events.mykola.kaiser.events.ui.read.ReadActivity;

import java.util.ArrayList;

public class MainActivity extends MvpAppCompatActivity implements IMainActivityView
{
    private ViewPager mViewPager;
    private FrameLayout mMainPage;
    private BottomNavigationView mNavigation;

    public static final int CODE_RESULT_MAINACTIVITY = 78;

    private MainViewPagersAdapter mAdapter;

    @InjectPresenter
    public MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Home", "Main onCreate");
        mMainPage = findViewById(R.id.main_frame_layout);

        mViewPager = findViewById(R.id.main_view_page);
        mAdapter = new MainViewPagersAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
                switch (i)
                {
                    case 0:
                        mPresenter.updateSearchAdapter();
                        mNavigation.setSelectedItemId(R.id.search_page);
                        break;
                    case 1:
                        mNavigation.setSelectedItemId(R.id.home_page);
                        break;
                    case 2:
                        mPresenter.clearCreatePage();
                        mNavigation.setSelectedItemId(R.id.create_page);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
        });

        mNavigation = findViewById(R.id.main_navigation_view);
        mNavigation.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId())
            {
                case R.id.search_page:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.home_page:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.create_page:
                    mViewPager.setCurrentItem(2);
                    break;
            }

            return true;
        });
        mNavigation.setSelectedItemId(R.id.home_page);
    }

    @Override
    public void addNewFragments(ArrayList<MvpAppCompatFragment> fragments)
    {
        Log.d("Home", "Main addNewFragments");
        mAdapter.addFragments(fragments);
    }

    @Override
    public void startWork()
    {
        Log.d("Home", "Main startWork isUser =" + mPresenter.isUser());
        if (!mPresenter.isUser())
        {
            mMainPage.setVisibility(View.INVISIBLE);
            MainDialogFragment dialogSignIn = new MainDialogFragment(mPresenter, mPresenter);
            dialogSignIn.show(getSupportFragmentManager(), MainDialogFragment.TAG);
        }
        else
        {
            mPresenter.userActivation();
        }
    }

    @Override
    public void turnOffPage()
    {
        Log.d("Home", "Main turnOffPage");
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void visibleDisplay() {
        mMainPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void startReadActivity(Event event, int index, boolean isOwner)
    {
        if (mPresenter.ismCanOpenActivity())
        {
            Intent intent = new Intent("ReadEvent");

            intent.putExtra(ReadActivity.KEY_INDEX, index);
            intent.putExtra(ReadActivity.KEY_ACTION, isOwner);

            mPresenter.setmCanOpenActivity(false);

            intent.putExtra(ReadActivity.READ , event);
            startActivityForResult(intent, CODE_RESULT_MAINACTIVITY);
        }
    }

    @Override
    public void signOut() {
        if (mPresenter.isCanClear())
        {
            startWork();
            mPresenter.setCanClear(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode)
        {
            case ReadActivity.READ_UPDATE:
                mPresenter.changeElement(data.getIntExtra("ID", 0));
                mViewPager.setCurrentItem(2);
                break;
            case ReadActivity.READ_DELETE:
                mPresenter.updateAdapter();
                break;
        }
    }
}
