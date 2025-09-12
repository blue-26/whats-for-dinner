package com.brw.demo.dto;

import java.util.List;

public class MealDTO {
    private Long id;
    private String name;
    private Long dayThemeId;
    private List<IngredientDTO> ingredients;
    private List<InstructionDTO> instructions;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getDayThemeId() { return dayThemeId; }
    public void setDayThemeId(Long dayThemeId) { this.dayThemeId = dayThemeId; }
    public List<IngredientDTO> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDTO> ingredients) { this.ingredients = ingredients; }
    public List<InstructionDTO> getInstructions() { return instructions; }
    public void setInstructions(List<InstructionDTO> instructions) { this.instructions = instructions; }
}
