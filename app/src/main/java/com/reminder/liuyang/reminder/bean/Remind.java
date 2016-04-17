package com.reminder.liuyang.reminder.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/26.
 */
public class Remind extends BmobObject implements Serializable {
    public int id;
    public BmobUser user;
    public String content;
    public long writeTime;
}
