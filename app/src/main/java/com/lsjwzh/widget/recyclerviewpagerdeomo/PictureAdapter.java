/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsjwzh.widget.recyclerviewpagerdeomo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;


class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.SimpleViewHolder> {
    private static final String TAG = "PictureAdapter";
    private final Context mContext;
    private List<String> mItems;
    private int mSelectedIndex = -1;

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.title);
        }
    }


    PictureAdapter(Context context, List<String> list) {
        mContext = context;
        mItems = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            addItem(i, list.get(i));
        }
    }

    private void addItem(int position, String fileInfo) {
        mItems.add(position, fileInfo);
        notifyItemInserted(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.picture_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: position is " + position + "\tmSelectedIndex is " + mSelectedIndex);
        String picturePath = mItems.get(position);
        if (picturePath.toLowerCase().endsWith(".gif")) {
            Glide.with(mContext).load(picturePath).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imageView);
        } else {
            Glide.with(mContext).load(picturePath).centerCrop().into(holder.imageView);
        }
        View itemView = holder.itemView;
        if (position == mSelectedIndex) {
            holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.item_background_press));
        } else {
            holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.item_background));

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedIndex = holder.getAdapterPosition();
//                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
//                holder.imageView.setBackground(R.drawable.item_background);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
