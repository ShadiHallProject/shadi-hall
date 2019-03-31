package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Account2Group implements Serializable {

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

    public void setAcGroupID(String acGroupID) {
        AcGroupID = acGroupID;
    }

    public void setAcTypeID(String acTypeID) {
        AcTypeID = acTypeID;
    }

    public void setAcGruopName(String acGruopName) {
        AcGruopName = acGruopName;
    }
}
