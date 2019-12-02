package org.by9steps.shadihall.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefrenceResturentSeekBar {
    String TABLE_GRID_VIEW_COL ="Grid_Col";
    String TABLE_ITEM_GRID_VIEW_COL="tableItemGrid_Col";

    Context context;



    SharedPreferences prefs;

    public PrefrenceResturentSeekBar(Context context) {
        this.context = context;
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTABLE_GRID_VIEW_COL() {
        return prefs.getString(TABLE_GRID_VIEW_COL,"3");
    }

    public void setTABLE_GRID_VIEW_COL(String value) {
        prefs.edit().putString(TABLE_GRID_VIEW_COL,value).apply();

    }
    public String getTABLE_ITEM_GRID_VIEW_COL() {
        return prefs.getString(TABLE_ITEM_GRID_VIEW_COL,"3");
    }

    public void setTABLE_ITEM_GRID_VIEW_COL(String value) {
        prefs.edit().putString(TABLE_ITEM_GRID_VIEW_COL,value).apply();
    }
}
