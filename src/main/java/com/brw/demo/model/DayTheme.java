package com.brw.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class DayTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String themeDescription;

    @OneToMany(mappedBy = "dayTheme", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Meal> meals;

    public DayTheme() {
    }

    public DayTheme(String name) {
        this.name = name;
    }

    public DayTheme(String name, String themeDescription) {
        this.name = name;
        this.themeDescription = themeDescription;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getThemeDescription() { return themeDescription; }
    public void setThemeDescription(String themeDescription) { this.themeDescription = themeDescription; }
    public List<Meal> getMeals() { return meals; }
    public void setMeals(List<Meal> meals) { this.meals = meals; }
}
