package com.important.events.mykola.kaiser.events.ui.home;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.model.User;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectHomeFragment;
import com.important.events.mykola.kaiser.events.model.interface_model.IReadAction;
import com.important.events.mykola.kaiser.events.model.interface_model.IUpdateViewHome;

@InjectViewState
public class HomeFragmentPresenter extends MvpPresenter<IHomeFragmentView> implements IUpdateViewHome
{
    private User user;

    private IConnectHomeFragment mConnectFragment;
    private IReadAction mIReadAction;

    public HomeFragmentPresenter(IConnectHomeFragment connectFragment, IReadAction iReadAction)
    {
        user = MyApp.get().getUser();
        mConnectFragment = connectFragment;
        mIReadAction = iReadAction;
    }

    public void connectFragment()
    {
        mConnectFragment.connectFragment(this);
    }

    public void changeList(boolean first)
    {
        if (first)
        {
            getViewState().changeRecyclerView(user.getSubscriptions());
        }
        else
        {
            getViewState().changeRecyclerView(user.getMyEvents());
        }
    }

    public void signOut()
    {
        mConnectFragment.signOutProfile();
    }

    @Override
    public void updateViewHome()
    {
        getViewState().updateProfile(user.getUri(), user.getName());
        changeList(false);
        getViewState().connectMinActivity(mIReadAction);
    }

    @Override
    public void updateHomeAdapter() {
        getViewState().updateHomeAdapter();
    }


}
