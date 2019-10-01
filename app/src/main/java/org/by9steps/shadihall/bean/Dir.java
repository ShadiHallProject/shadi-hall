package org.by9steps.shadihall.bean;


import org.by9steps.shadihall.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class Dir implements LayoutItemType {
    public String dirID;
    public String dirName;
    public String type;
    public String groupID;

    public Dir(String dirID, String dirName, String type) {
        this.dirID = dirID;
        this.dirName = dirName;
        this.type = type;
    }
    public Dir(String dirID, String dirName, String type, String groupID) {
        this.dirID = dirID;
        this.dirName = dirName;
        this.type = type;
        this.groupID = groupID;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
