package com.brw.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class DayTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dayOfWeek;

    @Column(nullable = false)
    private String theme;

    @OneToMany(mappedBy = "dayTheme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public List<Meal> getMeals() { return meals; }
    public void setMeals(List<Meal> meals) { this.meals = meals; }
}
