package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class TableSession extends SugarRecord {

    String tableName;
    String maxID;
    String updateDate;

    public TableSession() {
    }

    public TableSession(String tableName, String maxID, String updateDate) {
        this.tableName = tableName;
        this.maxID = maxID;
        this.updateDate = updateDate;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMaxID() {
        return maxID;
    }

    public void setMaxID(String maxID) {
        this.maxID = maxID;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
