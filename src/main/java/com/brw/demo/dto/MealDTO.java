package com.brw.demo.dto;

import java.util.List;

public class MealDTO {
    private Long id;
    private String name;
    private List<IngredientDTO> ingredients;
    private List<InstructionDTO> instructions;
    private DayThemeDTO dayTheme;

    public MealDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<IngredientDTO> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDTO> ingredients) { this.ingredients = ingredients; }
    public List<InstructionDTO> getInstructions() { return instructions; }
    public void setInstructions(List<InstructionDTO> instructions) { this.instructions = instructions; }
    public DayThemeDTO getDayTheme() { return dayTheme; }
    public void setDayTheme(DayThemeDTO dayTheme) { this.dayTheme = dayTheme; }
}
