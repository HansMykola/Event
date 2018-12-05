package com.important.events.mykola.kaiser.events.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.file.FileHelper;
import com.important.events.mykola.kaiser.events.model.Event;
import com.important.events.mykola.kaiser.events.model.User;
import com.important.events.mykola.kaiser.events.model.interface_model.IReadAction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>
{
    private IReadAction mIReadAction;
    private FileHelper mFileHelper;
    private ArrayList<Event> mEvents;
    private User mUser;

    public HomeRecyclerViewAdapter()
    {
        mEvents = new ArrayList<>();
        mFileHelper = new FileHelper();
        mUser = MyApp.get().getUser();
    }

    public void setIReadAction(IReadAction iReadAction)
    {
        mIReadAction = iReadAction;
    }

    public void setEvent(Event event)
    {
        this.mEvents.add(event);
        notifyDataSetChanged();
    }

    public void setListEvents(ArrayList<Event> events)
    {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.home_user_recycler_element,
                                    viewGroup,
                                    false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        Picasso.get().invalidate(mFileHelper.readImage(mEvents.get(i).getName()));
        Picasso.get().load(mFileHelper.readImage(mEvents.get(i).getName())).into(viewHolder.mImageEvent);
        viewHolder.mImageEvent.setScaleType(ImageView.ScaleType.FIT_XY);

        String allText = mEvents.get(i).getName() + "\n"
                + mEvents.get(i).getCategory() + "\n$ "
                + mEvents.get(i).getPrice() + "\n"
                + mEvents.get(i).getDate() + "\n"
                + mEvents.get(i).getLocation();

        viewHolder.allInfo.setText(allText);

        viewHolder.buttonMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mEvents.get(i).getLocation()
                    .replace(" ", "+"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            viewHolder.startIntent(mapIntent);
        });

        viewHolder.mImageEvent.setOnClickListener(v -> {
            /*Intent intent = new Intent("ReadEvent");

            if (mEvents.get(i).getIdOwner().equals(mUser.getId()))
            {
                intent.putExtra(ReadActivity.KEY_ACTION, true);
            }
            else
            {
                intent.putExtra(ReadActivity.KEY_ACTION, false);
            }

            intent.putExtra(ReadActivity.READ , mEvents.get(i));
            viewHolder.startIntent(intent);*/

            mIReadAction.startReadOrUpdate(mEvents.get(i), i,
                    mEvents.get(i).getIdOwner().equals(mUser.getId()));
        });
    }

    @Override
    public int getItemCount()
    {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mImageEvent;
        private TextView allInfo;
        private Button buttonMap;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mImageEvent = itemView.findViewById(R.id.image_event);
            allInfo = itemView.findViewById(R.id.text_all_info_for_event);
            buttonMap = itemView.findViewById(R.id.button_location_event);
        }

        public void startIntent(Intent intent)
        {
            itemView.getContext().startActivity(intent);
        }
    }
}