package com.reminder.liuyang.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.bean.Remind;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MainAdapter extends BaseAdapter {

    private List<Remind> mDatas;
    private LayoutInflater mInflater;
    private boolean isSearching;
    private String searchText;

    public MainAdapter(List<Remind> datas, Context context) {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_main, null);
            holder.tv_item_time = (TextView) convertView.findViewById(R.id.tv_item_time);
            holder.tv_item_describe = (TextView) convertView.findViewById(R.id.tv_item_describe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Remind remind = mDatas.get(position);
        holder.tv_item_time.setText(SystemUtils.formatTime(remind.writeTime));
        String describe = remind.content;
        int enterIndex = describe.indexOf('\n');
        if (enterIndex != -1) {
            describe = describe.substring(0, enterIndex);
        }

        if (isSearching) {
            int index = describe.indexOf(searchText);
            if (index != -1) {
                holder.tv_item_describe.setText(SystemUtils.getStyleText(describe, searchText));
            } else {
                holder.tv_item_describe.setText(describe);
            }
        } else {
            holder.tv_item_describe.setText(describe);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tv_item_time, tv_item_describe;
    }

    public void isSearching(boolean isSearching, String searchText) {
        this.isSearching = isSearching;
        this.searchText = searchText;
    }

}
