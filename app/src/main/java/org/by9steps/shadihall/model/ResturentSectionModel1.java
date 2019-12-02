package org.by9steps.shadihall.model;

import java.util.ArrayList;

public class ResturentSectionModel1 {
    private String sectionLabel;
    private ArrayList<joinQueryForResturent> itemArrayListJoinQuery;

    public ResturentSectionModel1(String sectionLabel, ArrayList<joinQueryForResturent> itemArrayListJoinQuery) {
        this.sectionLabel = sectionLabel;
        this.itemArrayListJoinQuery = itemArrayListJoinQuery;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<joinQueryForResturent> getItemArrayListJoinQuery() {
        return itemArrayListJoinQuery;
    }
}
