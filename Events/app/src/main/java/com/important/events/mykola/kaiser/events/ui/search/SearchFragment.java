package com.important.events.mykola.kaiser.events.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.interface_model.IConnectSearchFragment;
import com.important.events.mykola.kaiser.events.ui.search.dialogfragment.DialogFragmentSearch;

import java.util.ArrayList;

public class SearchFragment extends MvpAppCompatFragment implements ISearchFragmentView,
        View.OnClickListener {
    private EditText mEditSearch;

    private EventsRecyclerViewAdapter mAdapterFoundEvents;

    @InjectPresenter
    public SearchFragmentPresenter mPresenter;

    public SearchFragment() {
    }

    private IConnectSearchFragment mConnectSearchFragment;

    @SuppressLint("ValidFragment")
    // TODO Bad design - fragments should NOT have non-empty constructors.
    public SearchFragment(IConnectSearchFragment iConnectSearchFragment) {
        mConnectSearchFragment = iConnectSearchFragment;
    }

    @ProvidePresenter
    public SearchFragmentPresenter constructorPresenter() {
        return new SearchFragmentPresenter(mConnectSearchFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_search_fragment, container, false);

        ImageView mImageButtonSearch = view.findViewById(R.id.image_button_search);
        ImageView mImageButtonSetting = view.findViewById(R.id.image_button_setting);

        mImageButtonSearch.setOnClickListener(this);
        mImageButtonSetting.setOnClickListener(this);

        mEditSearch = view.findViewById(R.id.edit_name_event);

        RecyclerView mRecyclerCategory = view.findViewById(R.id.recycler_view_horizontal_category);
        LinearLayoutManager managerCategory = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        CategoryRecyclerViewAdapter mAdapterCategory = new CategoryRecyclerViewAdapter(mPresenter);
        mRecyclerCategory.setLayoutManager(managerCategory);
        mRecyclerCategory.setAdapter(mAdapterCategory);

        RecyclerView mRecyclerEvents = view.findViewById(R.id.recycler_view_event);
        mAdapterFoundEvents = new EventsRecyclerViewAdapter();
        mRecyclerEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerEvents.setAdapter(mAdapterFoundEvents);

        mPresenter.connect();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_button_search: {
                mPresenter.searchForName(mEditSearch.getText().toString());
                break;
            }
            case R.id.image_button_setting: {
                DialogFragmentSearch dialogSearch = new DialogFragmentSearch(mPresenter);
                dialogSearch.show(getFragmentManager(), DialogFragmentSearch.TAG);
                break;
            }
        }
    }

    @Override
    public void updateRecyclerViewAdapter() {
        mAdapterFoundEvents.notifyDataSetChanged();
    }

    @Override
    public void changeAdapter(ArrayList<Event> events) {
        mAdapterFoundEvents.setMEvents(events);
    }
}
