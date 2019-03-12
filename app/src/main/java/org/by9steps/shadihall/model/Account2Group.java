package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class Account2Group extends SugarRecord {

    String AcGroupID;
    String AcTypeID;
    String AcGruopName;

    public Account2Group(String acGroupID, String acTypeID, String acGruopName) {
        AcGroupID = acGroupID;
        AcTypeID = acTypeID;
        AcGruopName = acGruopName;
    }

    public Account2Group() {
    }

    public String getAcGroupID() {
        return AcGroupID;
    }

    public String getAcTypeID() {
        return AcTypeID;
    }

    public String getAcGruopName() {
        return AcGruopName;
    }
}
