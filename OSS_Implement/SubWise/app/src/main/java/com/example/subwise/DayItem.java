package com.example.subwise;

public class DayItem {
    private int day;
    private String serviceName;
    private int categoryColor;

    public DayItem(int day, String serviceName, int categoryColor) {
        this.day = day;
        this.serviceName = serviceName;
        this.categoryColor = categoryColor;
    }

    public int getDay() { return day; }
    public String getServiceName() { return serviceName; }
    public int getCategoryColor() { return categoryColor; }
}
