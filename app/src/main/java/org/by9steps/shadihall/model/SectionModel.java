package org.by9steps.shadihall.model;

import java.util.List;

public class SectionModel {
    String label;
    List<Menu> list;


    public SectionModel() {
    }

    public SectionModel(String label, List<Menu> list) {
        this.label = label;
        this.list = list;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Menu> getList() {
        return list;
    }

    public void setList(List<Menu> list) {
        this.list = list;
    }
}
