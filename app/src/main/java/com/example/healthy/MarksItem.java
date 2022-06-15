package com.example.healthy;

public class MarksItem {
    private String title;
    private String servings;
    private String likes;
    private String minutes;
    private String image;

    public MarksItem(String title, String servings, String likes, String minutes, String image) {
        this.title = title;
        this.servings = servings;
        this.likes = likes;
        this.minutes = minutes;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
