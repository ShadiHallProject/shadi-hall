package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class UpdateDate extends SugarRecord {

    String cbDate;
    String maxID;


    public UpdateDate(String cbDate, String maxID) {
        this.cbDate = cbDate;
        this.maxID = maxID;
    }

    public UpdateDate() {
    }

    public String getCbDate() {
        return cbDate;
    }

    public void setCbDate(String cbDate) {
        this.cbDate = cbDate;
    }

    public String getMaxID() {
        return maxID;
    }

    public void setMaxID(String maxID) {
        this.maxID = maxID;
    }
}
