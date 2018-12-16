package com.important.events.mykola.kaiser.events.ui.main.dialogfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.interface_model.IApiClient;
import com.important.events.mykola.kaiser.events.model.interface_model.IUserActivation;

public class MainDialogFragment extends MvpAppCompatDialogFragment
        implements IMainDialogFragmentView,
        GoogleApiClient.OnConnectionFailedListener {

    public final static String TAG = "DialogFragmentSignIn";
    private static final int REQUEST_CODE = 100;
    private GoogleApiClient mApiClient;

    public MainDialogFragment() {

    }

    private IUserActivation mIUserActivation;
    private IApiClient mIApiClient;

    @SuppressLint("ValidFragment")
    // TODO Bad design - fragments should NOT have non-empty constructors.
    public MainDialogFragment(IUserActivation mIUserActivation, IApiClient iApiClient) {
        this.mIUserActivation = mIUserActivation;
        this.mIApiClient = iApiClient;
    }

    @InjectPresenter
    public MainDialogFragmentPresenter mPresenter;

    @ProvidePresenter
    public MainDialogFragmentPresenter setDatePresenter() {
        return new MainDialogFragmentPresenter(mIUserActivation);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_dialog_fragment, container, false);

        if (mIApiClient.getAPIClient() == null) {
            GoogleSignInOptions options = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mApiClient = new GoogleApiClient
                    .Builder(MyApp.get())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                    .build();

            mIApiClient.setApiClient(mApiClient);
        } else {
            mApiClient = mIApiClient.getAPIClient();
        }

        SignInButton signInButton = view.findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(v -> signIn());

        return view;
    }

    public void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mApiClient);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mApiClient).setResultCallback(status -> {
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE: {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                mPresenter.handleResult(result);
                //signOut();
                break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void closeDialog() {
        signOut();
        getDialog().dismiss();
    }
}