package com.brw.demo.dto;

import java.util.List;

public class DayThemeDTO {
    private Long id;
    private String dayOfWeek;
    private String theme;
    private List<MealDTO> meals;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public List<MealDTO> getMeals() { return meals; }
    public void setMeals(List<MealDTO> meals) { this.meals = meals; }
}
