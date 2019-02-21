package org.by9steps.shadihall.bean;


import org.by9steps.shadihall.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class Dir implements LayoutItemType {
    public String dirID;
    public String dirName;
    public String btn;
    public String msgIc;

    public Dir(String dirID, String dirName) {
        this.dirID = dirID;
        this.dirName = dirName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
