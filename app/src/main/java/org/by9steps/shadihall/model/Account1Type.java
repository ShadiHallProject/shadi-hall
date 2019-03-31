package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Account1Type implements Serializable {

    String AcTypeID;
    String AcTypeName;

    public Account1Type(String acTypeID, String acTypeName) {
        AcTypeID = acTypeID;
        AcTypeName = acTypeName;
    }

    public Account1Type() {
    }

    public String getAcTypeID() {
        return AcTypeID;
    }

    public String getAcTypeName() {
        return AcTypeName;
    }

    public void setAcTypeID(String acTypeID) {
        AcTypeID = acTypeID;
    }

    public void setAcTypeName(String acTypeName) {
        AcTypeName = acTypeName;
    }
}
