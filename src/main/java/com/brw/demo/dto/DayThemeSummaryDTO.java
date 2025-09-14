package com.brw.demo.dto;

public class DayThemeSummaryDTO {
    private String day;
    private String themeDescription;

    public DayThemeSummaryDTO(String day, String themeDescription) {
        this.day = day;
        this.themeDescription = themeDescription;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getThemeDescription() {
        return themeDescription;
    }

    public void setThemeDescription(String themeDescription) {
        this.themeDescription = themeDescription;
    }
}
