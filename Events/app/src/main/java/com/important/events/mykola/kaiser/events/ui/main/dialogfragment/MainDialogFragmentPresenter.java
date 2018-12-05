package com.important.events.mykola.kaiser.events.ui.main.dialogfragment;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.important.events.mykola.kaiser.events.database.SQLiteDatabaseEvent;
import com.important.events.mykola.kaiser.events.model.User;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.model.interface_model.IUserActivation;

@InjectViewState
public class MainDialogFragmentPresenter extends MvpPresenter<IMainDialogFragmentView>
{
    private IUserActivation mIUserActivation;

    public MainDialogFragmentPresenter(IUserActivation mIUserActivation)
    {
        this.mIUserActivation = mIUserActivation;
    }

    public void handleResult(GoogleSignInResult result)
    {
        Log.d("Home", "DialogMainP handleResult");
        if (result.isSuccess())
        {
            User user = MyApp.get().getUser();
            GoogleSignInAccount account = result.getSignInAccount();

            SQLiteDatabaseEvent database = MyApp.get().getDatabaseEvent();

            User isUser = database.getUser(account.getId());

            user.setId(account.getId());
            user.setName(account.getDisplayName());

            try
            {
                user.setUri(account.getPhotoUrl().toString());
            }
            catch (Exception e)
            {

            }

            if (isUser != null)
            {
                user.searchMyEvent(database.getEvents(user.getId(), true));
            }

            mIUserActivation.userActivation();

            getViewState().closeDialog();
        }
    }
}
