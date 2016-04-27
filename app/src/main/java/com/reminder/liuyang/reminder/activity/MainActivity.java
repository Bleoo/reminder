package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.adapter.MainAdapter;
import com.reminder.liuyang.reminder.bean.Remind;
import com.reminder.liuyang.reminder.utils.DBUtils;
import com.reminder.liuyang.reminder.utils.SystemUtils;
import com.reminder.liuyang.reminder.view.DeleteDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REFRESH_All_LIST = 1;
    private static final int REFRESH_SEARCH_LIST = 2;

    private SwipeMenuListView smlv_main;
    private EditText et_search;
    private View btn_search_clear;
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
        et_search = (EditText) findViewById(R.id.et_search);
        btn_search_clear = findViewById(R.id.btn_search_clear);
        iv_empty = findViewById(R.id.iv_empty);

        findViewById(R.id.rl_setting).setOnClickListener(this);
        findViewById(R.id.rl_writing).setOnClickListener(this);
        btn_search_clear.setOnClickListener(this);

        adapter = new MainAdapter(mData, mContext);
        smlv_main.setAdapter(adapter);
        initSwipeMenuListView();
        initSearchEditText();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH_All_LIST:
                        adapter.notifyDataSetChanged();
                        showViewByData();
                        break;
                    case REFRESH_SEARCH_LIST:
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        dbUtils = new DBUtils(this);
        refreshListAllData();
    }

    private void initSearchEditText() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SystemUtils.toggleSoftInput(mContext, et_search, false);
                }
                return false;
            }
        });
    }

    private void searchData() {
        String searchText = et_search.getText().toString();
        if (searchText.length() > 0) {
            btn_search_clear.setVisibility(View.VISIBLE);
            adapter.isSearching(true, searchText);
            refreshListSearchData(searchText);
        } else {
            btn_search_clear.setVisibility(View.INVISIBLE);
            adapter.isSearching(false, null);
            refreshListAllData();
        }
    }

    private void refreshListAllData() {
        new Thread() {
            @Override
            public void run() {
                mData.clear();
                mData.addAll(dbUtils.query());
                Message.obtain(handler, REFRESH_All_LIST).sendToTarget();
            }
        }.start();
    }

    private void refreshListSearchData(final String searchText) {
        new Thread() {
            @Override
            public void run() {
                mData.clear();
                mData.addAll(dbUtils.search(searchText));
                Message.obtain(handler, REFRESH_SEARCH_LIST).sendToTarget();
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
                if (et_search.getText().length() > 0) {
                    intent.putExtra("searchText", et_search.getText().toString());
                }
                startActivity(intent);
            }
        });
    }

    private void delete(int position) {
        if (dbUtils.delete(mData.get(position)) > 0) {
            mData.remove(position);
            Message.obtain(handler, REFRESH_All_LIST).sendToTarget();
        }
    }

    private void showViewByData() {
        if (mData.size() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
            et_search.setVisibility(View.INVISIBLE);
        } else {
            iv_empty.setVisibility(View.INVISIBLE);
            et_search.setVisibility(View.VISIBLE);
            et_search.clearFocus();
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
            case R.id.btn_search_clear:
                et_search.setText("");
                SystemUtils.toggleSoftInput(mContext, et_search, false);
                break;
        }
    }

    @Override
    protected void onPause() {
        SystemUtils.toggleSoftInput(mContext, et_search, false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_search.clearFocus();
        searchData();
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
