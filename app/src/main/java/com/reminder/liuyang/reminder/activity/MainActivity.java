package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.adapter.MainAdapter;
import com.reminder.liuyang.reminder.bean.Remind;
import com.reminder.liuyang.reminder.utils.DBUtils;
import com.reminder.liuyang.reminder.view.DeleteDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REFRESH_LIST = 1;

    private SwipeMenuListView smlv_main;
    private View iv_empty;
    private DeleteDialog deleteDialog;

    private List<Remind> mData = new ArrayList<>();
    private MainAdapter adapter;
    private DBUtils dbUtils;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smlv_main = (SwipeMenuListView) findViewById(R.id.smlv_main);
        iv_empty = findViewById(R.id.iv_empty);

        findViewById(R.id.rl_setting).setOnClickListener(this);
        findViewById(R.id.rl_writing).setOnClickListener(this);

        adapter = new MainAdapter(mData, mContext);
        smlv_main.setAdapter(adapter);
        initSwipeMenuListView();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case REFRESH_LIST:
                        adapter.notifyDataSetChanged();
                        showViewByData();
                        break;
                }
            }
        };

        dbUtils = new DBUtils(this);

        new Thread(){
            @Override
            public void run() {
                mData.clear();
                mData.addAll(dbUtils.query());
                Message.obtain(handler, REFRESH_LIST).sendToTarget();
            }
        }.start();
    }

    private void initSwipeMenuListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(R.color.red);
                openItem.setWidth(90);
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        smlv_main.setMenuCreator(creator);
        smlv_main.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        showDeleteDialog(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        smlv_main.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        smlv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WriteActivity.class);
                intent.putExtra("remind", mData.get(position));
                startActivity(intent);
            }
        });
    }

    private void delete(int position) {
        if (dbUtils.delete(mData.get(position)) > 0) {
            mData.remove(position);
            Message.obtain(handler, REFRESH_LIST).sendToTarget();
        }
    }

    private void showViewByData() {
        if (mData.size() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
        } else {
            iv_empty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_writing:
                intent = new Intent(this, WriteActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mData.clear();
        mData.addAll(dbUtils.query());
        Message.obtain(handler, REFRESH_LIST).sendToTarget();
    }

    private void showDeleteDialog(final int position) {
        if (deleteDialog == null) {
            deleteDialog = new DeleteDialog(this);
        }
        deleteDialog.setDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                delete(position);
            }
        });
        deleteDialog.show();
    }
}
