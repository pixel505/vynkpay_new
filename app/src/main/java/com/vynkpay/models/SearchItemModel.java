package com.vynkpay.models;

public class SearchItemModel {
    String name;
    int icon;
    Class<?> launchingActivity;

    public SearchItemModel(String name, int icon, Class<?> launchingActivity) {
        this.name = name;
        this.icon = icon;
        this.launchingActivity = launchingActivity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class<?> getLaunchingActivity() {
        return launchingActivity;
    }

    public void setLaunchingActivity(Class<?> launchingActivity) {
        this.launchingActivity = launchingActivity;
    }
}
