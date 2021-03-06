package com.lsjwzh.widget.recyclerviewpagerdeomo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

public class VerticalPagerPictureActivity extends Activity {
    protected RecyclerViewPager mRecyclerView;
    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_vertical_viewpager);
        initData();
        initViewPager();
    }

    private void initData() {
        if (getIntent() != null && getIntent().hasExtra("pictures")) {
            mList = getIntent().getStringArrayListExtra("pictures");
        }
    }

    protected void initViewPager() {
        mRecyclerView = (RecyclerViewPager) findViewById(R.id.viewpager);

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setTriggerOffset(0.15f);
        mRecyclerView.setFlingFactor(0.25f);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(new PictureAdapter(this, mList));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = mRecyclerView.getChildCount();
                int width = mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                    }
                }
            }
        });

//        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (mRecyclerView.getChildCount() < 3) {
//                    if (mRecyclerView.getChildAt(1) != null) {
//                        View v1 = mRecyclerView.getChildAt(1);
//                        v1.setScaleY(0.9f);
//                    }
//                } else {
//                    if (mRecyclerView.getChildAt(0) != null) {
//                        View v0 = mRecyclerView.getChildAt(0);
//                        v0.setScaleY(0.9f);
//                    }
//                    if (mRecyclerView.getChildAt(2) != null) {
//                        View v2 = mRecyclerView.getChildAt(2);
//                        v2.setScaleY(0.9f);
//                    }
//                }
//
//            }
//        });
    }
}
