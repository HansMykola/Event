package com.important.events.mykola.kaiser.events.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectHomeFragment;
import com.important.events.mykola.kaiser.events.model.interface_model.IReadAction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends MvpAppCompatFragment implements IHomeFragmentView
{
    private CircleImageView mFaceCircleImage;
    private RecyclerView mRecyclerEvents;
    private ImageView mImageExit;
    private TextView mFullName;
    private Button mButtonMyEvents, mButtonSubscriptions;

    private HomeRecyclerViewAdapter mAdapter;

    @InjectPresenter
    public HomeFragmentPresenter mPresenter;

    @ProvidePresenter
    public HomeFragmentPresenter constructorPresenter()
    {
        return new HomeFragmentPresenter(mConnectFragment, mIReadAction);
    }

    public HomeFragment()
    { }

    private IConnectHomeFragment mConnectFragment;
    private IReadAction mIReadAction;

    @SuppressLint("ValidFragment")
    public HomeFragment(IConnectHomeFragment connectFragment, IReadAction iReadAction)
    {
        mConnectFragment = connectFragment;
        mIReadAction = iReadAction;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.main_home_fragment, container, false);

        mFaceCircleImage = view.findViewById(R.id.image_face);
        mRecyclerEvents = view.findViewById(R.id.events_recycler_view);
        mFullName = view.findViewById(R.id.main_name);

        mButtonMyEvents = view.findViewById(R.id.button_subscriptions);

        mButtonMyEvents.setOnClickListener(v -> mPresenter.changeList(true));

        mButtonSubscriptions = view.findViewById(R.id.button_my_events);

        mButtonSubscriptions.setOnClickListener(v -> mPresenter.changeList(false));

        mAdapter = new HomeRecyclerViewAdapter();
        mRecyclerEvents.setAdapter(mAdapter);
        mRecyclerEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        mImageExit = view.findViewById(R.id.image_exit);

        mImageExit.setOnClickListener(v -> mPresenter.signOut());

        mPresenter.connectFragment();

        return view;
    }

    @Override
    public void connectMinActivity(IReadAction iReadAction) {
        mAdapter.setIReadAction(iReadAction);
    }

    @Override
    public void updateHomeAdapter()
    {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateProfile(String uri, String name)
    {
        Picasso.get().invalidate(uri);
        Picasso.get().load(uri).into(mFaceCircleImage);
        mFullName.setText(name);
    }

    @Override
    public void changeRecyclerView(ArrayList<Event> events)
    {
        mAdapter.setListEvents(events);
    }
}
