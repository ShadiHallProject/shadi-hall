package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class AreaName extends SugarRecord {

    String AreaID;
    String AreaInID;
    String AreaName;

    public AreaName() {
    }

    public AreaName(String areaID, String areaInID, String areaName) {
        AreaID = areaID;
        AreaInID = areaInID;
        AreaName = areaName;
    }

    public String getAreaID() {
        return AreaID;
    }

    public String getAreaInID() {
        return AreaInID;
    }

    public String getAreaName() {
        return AreaName;
    }
}
