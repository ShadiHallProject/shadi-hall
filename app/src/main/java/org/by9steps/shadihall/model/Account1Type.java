package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class Account1Type extends SugarRecord {
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
}
