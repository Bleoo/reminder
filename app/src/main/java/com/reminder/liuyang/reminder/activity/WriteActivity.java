package com.reminder.liuyang.reminder.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.bean.Remind;
import com.reminder.liuyang.reminder.utils.DBUtils;
import com.reminder.liuyang.reminder.utils.SystemUtils;
import com.reminder.liuyang.reminder.view.DeleteDialog;

/**
 * Created by Administrator on 2016/3/27.
 */
public class WriteActivity extends BaseActivity implements View.OnClickListener {

    private final static int EDITING = 0;
    private final static int EDIT_FINISH = 1;

    private TextView tv_write_time;
    private EditText et_content;
    private View rl_save;
    private View tv_delete;
    private DeleteDialog deleteDialog;

    private DBUtils dbUtils;
    private Remind remind;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        tv_write_time = (TextView) findViewById(R.id.tv_write_time);
        et_content = (EditText) findViewById(R.id.et_content);
        rl_save = findViewById(R.id.rl_save);
        tv_delete = findViewById(R.id.tv_delete);

        rl_save.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        et_content.setOnClickListener(this);
        findViewById(R.id.tv_back_list).setOnClickListener(this);

        dbUtils = new DBUtils(this);

        bindData();
    }

    private void bindData() {
        if (getIntent().hasExtra("remind")) {
            remind = (Remind) getIntent().getSerializableExtra("remind");
            tv_write_time.setText(SystemUtils.formatTime(remind.writeTime));
            if (getIntent().hasExtra("searchText")) {
                searchText = getIntent().getStringExtra("searchText");
                et_content.setText(SystemUtils.getStyleText(remind.content, searchText));
            } else {
                et_content.setText(remind.content);
            }
            setWriteStatus(EDIT_FINISH);
        } else {
            tv_write_time.setText(SystemUtils.formatTime(System.currentTimeMillis()));
            setWriteStatus(EDITING);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SystemUtils.toggleSoftInput(mContext, et_content, true);
                }
            }, 300);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_save:
                save();
                break;
            case R.id.tv_delete:
                showDeleteDialog();
                break;
            case R.id.tv_back_list:
                SystemUtils.toggleSoftInput(mContext, et_content, false);
                finish();
                break;
            case R.id.et_content:
                setWriteStatus(EDITING);
                break;
        }
    }

    private void delete() {
        if (remind != null && remind.id != 0) {
            dbUtils.delete(remind);
            SystemUtils.toggleSoftInput(mContext, et_content, false);
            finish();
        }
    }

    private void save() {
        String content = et_content.getText().toString();
        if (content.isEmpty()) {
            SystemUtils.toggleSoftInput(mContext, et_content, false);
            finish();
            return;
        }
        if (remind == null) {
            remind = new Remind();
        }
        remind.content = content;
        remind.writeTime = System.currentTimeMillis();
        if (remind.id == 0) {
            dbUtils.save(remind);
        } else {
            dbUtils.update(remind);
        }
        setWriteStatus(EDIT_FINISH);
    }

    private void setWriteStatus(int writeStatus) {
        switch (writeStatus) {
            case EDITING:
                et_content.setFocusable(true);
                et_content.setFocusableInTouchMode(true);
                et_content.requestFocus();
                SystemUtils.toggleSoftInput(mContext, et_content, true);
                tv_delete.setVisibility(View.INVISIBLE);
                rl_save.setVisibility(View.VISIBLE);
                break;
            case EDIT_FINISH:
                et_content.setFocusable(false);
                SystemUtils.toggleSoftInput(mContext, et_content, false);
                tv_delete.setVisibility(View.VISIBLE);
                rl_save.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void showDeleteDialog() {
        if (deleteDialog == null) {
            deleteDialog = new DeleteDialog(this);
            deleteDialog.setDeleteClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    delete();
                }
            });
        }
        deleteDialog.show();
    }
}
