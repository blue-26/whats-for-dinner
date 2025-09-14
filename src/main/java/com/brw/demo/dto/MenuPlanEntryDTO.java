package com.brw.demo.dto;

public class MenuPlanEntryDTO {
    private String day;
    private String theme;
    private MealDTO meal;

    public MenuPlanEntryDTO(String day, String theme, MealDTO meal) {
        this.day = day;
        this.theme = theme;
        this.meal = meal;
    }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public MealDTO getMeal() { return meal; }
    public void setMeal(MealDTO meal) { this.meal = meal; }
}
