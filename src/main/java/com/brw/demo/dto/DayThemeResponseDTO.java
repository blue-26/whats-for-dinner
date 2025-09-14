package com.brw.demo.dto;

import java.util.List;

public class DayThemeResponseDTO {
    private String day;
    private String theme;
    private List<MealDTO> meals;

    public DayThemeResponseDTO(String day, String theme, List<MealDTO> meals) {
        this.day = day;
        this.theme = theme;
        this.meals = meals;
    }

    // Getters and Setters
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<MealDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<MealDTO> meals) {
        this.meals = meals;
    }
}
