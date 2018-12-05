package com.important.events.mykola.kaiser.events.ui.search;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.important.events.mykola.kaiser.events.database.SQLiteDatabaseEvent;
import com.important.events.mykola.kaiser.events.file.FileHelper;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.User;
import com.important.events.mykola.kaiser.events.ui.read.ReadActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder>
{
    private SQLiteDatabaseEvent mDatabaseEvent;
    private FileHelper mFileHelper;
    private ArrayList<Event> mEvents;

    public EventsRecyclerViewAdapter()
    {
        Log.d("Home", "Search EventsRecyclerViewAdapter");
        mDatabaseEvent = MyApp.get().getDatabaseEvent();
        mFileHelper = new FileHelper();
        mEvents = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        Log.d("Home", "Search onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_recycler_element,
                        viewGroup,
                        false);
        return new EventsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        Log.d("Home", "Search onBindViewHolder");
        Picasso.get().load(mFileHelper.readImage(mEvents.get(i).getName())).into(viewHolder.mImageEvent);


        User user = MyApp.get().getUser();

        String allText = mEvents.get(i).getName() + "\n"
                + mEvents.get(i).getCategory() + "\n"
                + mEvents.get(i).getPrice() + "\n"
                + mEvents.get(i).getDate() + "\n"
                + mEvents.get(i).getLocation();

        viewHolder.mAllInfo.setText(allText);

        if ((mEvents.get(i).getIdOwner().equals(user.getId()))
                || (user.isInSubscriptions(mEvents.get(i).getId())))
        {
            Resources resources = MyApp.get().getResources();
            viewHolder.mToSubscribe.setImageDrawable(resources
                                    .getDrawable(R.drawable.ic_subscribe_turned_in_black_24dp,null));
        }

        viewHolder.mToSubscribe.setOnClickListener(v -> {
            if (!mEvents.get(i).getIdOwner().equals(user.getId()))
            {
                if (!user.isInSubscriptions(mEvents.get(i).getId()))
                {
                    mDatabaseEvent.insertUserEvent(user, mEvents.get(i));
                    user.setASubscription(mEvents.get(i));
                    ((ImageView) v).setImageDrawable(MyApp.get()
                            .getResources()
                            .getDrawable(
                                    R.drawable.ic_subscribe_turned_in_black_24dp,
                                    null));
                }
                else
                {
                    mDatabaseEvent.deleteUserEvent(user);
                    user.deleteEvents(user.getSubscriptions(), mEvents.get(i));
                    ((ImageView) v).setImageDrawable(MyApp.get()
                            .getResources()
                            .getDrawable(
                                    R.drawable.ic_add_circle_black_24dp,
                                    null));
                }

                /*if (!(user.getSubscriptions().contains(mEvents.get(i)))
                        && !(user.getMyEvents().contains(mEvents.get(i))))
                {
                    user.setASubscription(mEvents.get(i));
                    ((ImageView) v).setImageDrawable(MyApp.get()
                            .getResources()
                            .getDrawable(
                                    R.drawable.ic_subscribe_turned_in_black_24dp,
                                    null));
                }
                else
                {
                    user.getSubscriptions().remove(mEvents.get(i));
                    ((ImageView) v).setImageDrawable(MyApp.get()
                            .getResources()
                            .getDrawable(
                                    R.drawable.ic_add_circle_black_24dp,
                                    null));
                }*/
            }
        });

        viewHolder.mButtonMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mEvents.get(i).getLocation()
                    .replace(" ", "+"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            viewHolder.startIntent(mapIntent);
        });

        viewHolder.mLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent("ReadEvent");
            intent.putExtra(ReadActivity.READ , mEvents.get(i));
            viewHolder.startIntent(intent);
        });
    }

    public void setMEvents(ArrayList<Event> events)
    {
        Log.d("Home", "Search setMEvents");
        mEvents.clear();
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout mLinearLayout;
        private ImageView mImageEvent, mToSubscribe;
        private TextView mAllInfo;
        private Button mButtonMap;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mLinearLayout = itemView.findViewById(R.id.element_linear);
            mImageEvent = itemView.findViewById(R.id.image_event);
            mAllInfo = itemView.findViewById(R.id.text_all_info_for_event);
            mButtonMap = itemView.findViewById(R.id.button_location_event);
            mToSubscribe = itemView.findViewById(R.id.image_button_add);
        }

        public void startIntent(Intent intent)
        {
            itemView.getContext().startActivity(intent);
        }

    }
}
