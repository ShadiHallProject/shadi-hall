package org.by9steps.shadihall.model;

public class Menu {

    String title;
    int image;

    public Menu(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
