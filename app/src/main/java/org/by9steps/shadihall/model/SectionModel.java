package org.by9steps.shadihall.model;

import java.util.List;

public class SectionModel {
    String label;
    List<ProjectMenu> list;


    public SectionModel() {
    }

    public SectionModel(String label, List<ProjectMenu> list) {
        this.label = label;
        this.list = list;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ProjectMenu> getList() {
        return list;
    }

    public void setList(List<ProjectMenu> list) {
        this.list = list;
    }
}
