package org.by9steps.shadihall.model;

public class ResturentSectionModel3 {
    public static final int HEADER_TYPE = 1;
    public static final int HEADER_EVENT_TYPE = 0;

    private String sectionLabel;
    private int mType;
    private joinQueryForResturentAddOrder itemArrayListJoinQuery;
    private int quantity=0;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ResturentSectionModel3(String sectionLabel, int mType, joinQueryForResturentAddOrder itemArrayListJoinQuery) {
        this.sectionLabel = sectionLabel;
        this.mType = mType;
        this.itemArrayListJoinQuery = itemArrayListJoinQuery;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public joinQueryForResturentAddOrder getItemArrayListJoinQuery() {
        return itemArrayListJoinQuery;
    }

    public void setItemArrayListJoinQuery(joinQueryForResturentAddOrder itemArrayListJoinQuery) {
        this.itemArrayListJoinQuery = itemArrayListJoinQuery;
    }
}
