package org.by9steps.shadihall.bean;


import org.by9steps.shadihall.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class File implements LayoutItemType {
    public String fileID;
    public String fileName;
    public String btn;
    public String filePho;

    public File(String fileID, String fileName, String btn, String filePho) {
        this.fileID = fileID;
        this.fileName = fileName;
        this.btn = btn;
        this.filePho = filePho;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }
}
