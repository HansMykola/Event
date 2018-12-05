package com.important.events.mykola.kaiser.events.ui.search;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.important.events.mykola.kaiser.events.MyApp;
import com.important.events.mykola.kaiser.events.R;
import com.important.events.mykola.kaiser.events.model.interface_model.IChooseCategory;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>
{
    private IChooseCategory mIChooseCategory;
    private ArrayList<Drawable> mListImageEvent;
    private String[] mtextCategory;

    public CategoryRecyclerViewAdapter(IChooseCategory chooseCategory)
    {
        mListImageEvent = new ArrayList<>();

        Resources resources = MyApp.get().getResources();
        mListImageEvent.add(resources.getDrawable(R.drawable.picture_art, null));
        mListImageEvent.add(resources.getDrawable(R.drawable.picture_sport, null));
        mListImageEvent.add(resources.getDrawable(R.drawable.picture_it, null));
        mListImageEvent.add(resources.getDrawable(R.drawable.picture_science, null));
        mListImageEvent.add(resources.getDrawable(R.drawable.picture_charity, null));

        mtextCategory = resources.getStringArray(R.array.category);

        mIChooseCategory = chooseCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup
                                        .getContext())
                                        .inflate(R.layout.search_category_recycler_element,
                                                            viewGroup,
                                                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        viewHolder.mCircleCategory.setImageDrawable(mListImageEvent.get(i));
        viewHolder.mTextCategory.setText(mtextCategory[i]);
        viewHolder.mLinearCard.setOnClickListener(v -> {
            mIChooseCategory.chooseCategory(mtextCategory[i]);
        });
    }

    @Override
    public int getItemCount()
    {
        return mListImageEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView mCircleCategory;
        private LinearLayout mLinearCard;
        private TextView mTextCategory;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mCircleCategory = itemView.findViewById(R.id.image_circle_category);
            mTextCategory = itemView.findViewById(R.id.text_category_item);
            mLinearCard = itemView.findViewById(R.id.linear_card_category);
        }
    }
}
