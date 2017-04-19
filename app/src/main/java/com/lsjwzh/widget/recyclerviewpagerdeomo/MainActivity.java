/*
 * Copyright (C) 2015 lsjwzh
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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.adapter.GenericRecyclerViewAdapter;
import com.lsjwzh.adapter.OnRecyclerViewItemClickListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int EXTERNAL_STORAGE_REQ_CODE = 10;
    RecyclerView mDemoRecyclerView;
    private DemoListAdapter mDemoListAdapter;
    private ProgressBar mProgressBar;
    private ArrayList<String> currentFileList;
    private boolean enableEvent = true;
    private String pictureDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_list);
        mDemoRecyclerView = (RecyclerView) findViewById(R.id.demo_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mDemoRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mDemoListAdapter = new DemoListAdapter();
        mDemoRecyclerView.setAdapter(mDemoListAdapter);
        mDemoListAdapter.add(new DemoItem("Single Fling Pager(like official ViewPager)") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, SingleFlingPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Free Fling Pager(like ViewPager combine with Gallary)") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, FreeFlingPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Material Demo") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, MaterialDemoActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Material Demo With loop pager") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, MaterialDemoWithLoopPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Vertical ViewPager Demo") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, VerticalPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Loop ViewPager Demo") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, LoopPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Reverse Single Fling Pager(like official ViewPager)") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, ReverseSingleFlingPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("Reverse Vertical ViewPager Demo") {
            @Override
            void onClick() {
                startActivity(new Intent(MainActivity.this, ReverseVerticalPagerActivity.class));
            }
        });
        mDemoListAdapter.add(new DemoItem("3D effect Demo(TODO)") {
            @Override
            void onClick() {
                // TODO: open 3D effect Demo
            }
        });
        mDemoListAdapter.add(new DemoItem("Picture Demo") {
            @Override
            void onClick() {
                Onclick_Picture();
            }
        });
    }

    private void Onclick_Picture() {
        //如果 用户未赋予读取权限 需要获取
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getFileList: 没有权限");
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {
            LoadTask loadTask = new LoadTask();
            loadTask.execute(pictureDir);
        }
    }

    class DemoListAdapter extends GenericRecyclerViewAdapter<DemoItem, DemoListItemViewHolder> {

        DemoListAdapter() {
            setOnItemClickListener(new OnRecyclerViewItemClickListener<DemoListItemViewHolder>() {
                @Override
                public void onItemClick(View view, int position, DemoListItemViewHolder viewHolder) {
                    getItem(position).onClick();
                }
            });
        }

        @Override
        public DemoListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.demo_list_item, parent, false);
            return new DemoListItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DemoListItemViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.mTextView.setText(getItem(position).mText);
        }
    }

    class DemoItem {
        String mText;

        DemoItem(String text) {
            mText = text;
        }

        void onClick() {
        }
    }

    class DemoListItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public DemoListItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    class LoadTask extends AsyncTask<String, Integer, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            Log.d(TAG, "doInBackground() started : " + params[0]);
            return getFileList(params[0]);
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute() ...");
            mProgressBar.setVisibility(View.VISIBLE);
            enableEvent = true;

        }

        @Override
        protected void onPostExecute(List<String> result) {
            Log.d(TAG, "onPostExecute() returned: " + result);
            currentFileList = (ArrayList<String>) result;
            mProgressBar.setVisibility(View.GONE);
            enableEvent = true;
            Intent intent = new Intent(MainActivity.this, VerticalPagerPictureActivity.class);
            intent.putStringArrayListExtra("pictures", currentFileList);
            startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled() ...");
        }

        private List<String> getFileList(String filePath) {
            Log.d(TAG, "getFileList: " + filePath);
            ArrayList<String> list = new ArrayList<>();

            list.addAll(getFilesList(filePath));

            return list;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        if (requestCode == EXTERNAL_STORAGE_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Onclick_Picture();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.requestWritePermissionDenied), Toast.LENGTH_LONG).show();
            }
        }
    }

    public ArrayList<String> getFilesList(String filePath) {
        ArrayList<String> list = new ArrayList<>();
        File file = new File(filePath);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(".")) {
                    return false;
                }
                return true;
            }
        });
        if (files == null) {
            Log.d(TAG, "getFileList: files null ");
        } else {
            ArrayList<FileInfo> fs = new ArrayList<>();
            for (File child : files) {
                if (!child.isDirectory() && !child.isHidden()) {
                    if (child.getAbsolutePath().toLowerCase().endsWith(".png") || child.getAbsolutePath().toLowerCase().endsWith(".jpg")) {
                        FileInfo info = new FileInfo();
                        info.fileName = child.getName();
                        info.filePath = child.getAbsolutePath();
                        info.ModifiedDate = child.lastModified();
                        info.create_time = info.ModifiedDate;
                        info.fileSize = child.length();
                        fs.add(info);
                    }

                }
            }
            for (int i = 0; i < fs.size(); i++) {
                list.add(fs.get(i).filePath);
            }
        }
        return list;
    }
}
