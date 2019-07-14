package org.by9steps.shadihall.model;

import android.content.ServiceConnection;

import java.io.Serializable;

public class Projects implements Serializable {

    String ProjectID;
    String ProjectName;
    String ProjectType;

    public Projects(String projectID, String projectName, String projectType) {
        ProjectID = projectID;
        ProjectName = projectName;
        ProjectType = projectType;
    }

    public Projects() {
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectType() {
        return ProjectType;
    }

    public void setProjectType(String projectType) {
        ProjectType = projectType;
    }
}
