package org.by9steps.shadihall.model;

import java.io.Serializable;

public class ProjectMenu implements Serializable {

    String MenuID;
    String ProjectID;
    String MenuGroup;
    String MenuName;
    String PageOpen;
    String ValuePass;
    String ImageName;
    String GroupSortBy;
    String SortBy;

    private int isRow;

    public ProjectMenu() {
    }

    public ProjectMenu(String menuID, String projectID, String menuGroup, String menuName, String pageOpen, String valuePass, String imageName, String groupSortBy, String sortBy) {
        MenuID = menuID;
        ProjectID = projectID;
        MenuGroup = menuGroup;
        MenuName = menuName;
        PageOpen = pageOpen;
        ValuePass = valuePass;
        ImageName = imageName;
        GroupSortBy = groupSortBy;
        SortBy = sortBy;
    }

    public static ProjectMenu createRow(String menuID, String projectID, String menuGroup, String menuName, String pageOpen, String valuePass, String imageName, String groupSortBy, String sortBy){
        ProjectMenu projectMenu = new ProjectMenu();

        projectMenu.isRow = 1;
        projectMenu.MenuID = menuID;
        projectMenu.ProjectID = projectID;
        projectMenu.MenuGroup = menuGroup;
        projectMenu.MenuName = menuName;
        projectMenu.PageOpen = pageOpen;
        projectMenu.ValuePass = valuePass;
        projectMenu.ImageName = imageName;
        projectMenu.GroupSortBy = groupSortBy;
        projectMenu.SortBy = sortBy;

        return projectMenu;
    }

    public static ProjectMenu createSection(String menuGroup) {
        ProjectMenu projectMenu = new ProjectMenu();

        projectMenu.isRow = 0;
        projectMenu.MenuGroup = menuGroup;

        return projectMenu;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getMenuGroup() {
        return MenuGroup;
    }

    public void setMenuGroup(String menuGroup) {
        MenuGroup = menuGroup;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getPageOpen() {
        return PageOpen;
    }

    public void setPageOpen(String pageOpen) {
        PageOpen = pageOpen;
    }

    public String getValuePass() {
        return ValuePass;
    }

    public void setValuePass(String valuePass) {
        ValuePass = valuePass;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getGroupSortBy() {
        return GroupSortBy;
    }

    public void setGroupSortBy(String groupSortBy) {
        GroupSortBy = groupSortBy;
    }

    public String getSortBy() {
        return SortBy;
    }

    public void setSortBy(String sortBy) {
        SortBy = sortBy;
    }

    public int isRow() {
        return isRow;
    }
}
