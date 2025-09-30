package com.brw.demo.controller;

import com.brw.demo.dto.IngredientDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.model.Ingredient;
import com.brw.demo.service.MealService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuPlannerController.class)
class MenuPlannerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @Test
    void getIngredientsForMeal_returnsJson() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Eggs");
        ingredient.setQuantity("2");

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Omelette");
        meal.setIngredients(List.of(ingredient));
        DayTheme theme = new DayTheme("Monday", "Breakfast Bonanza");
        meal.setDayTheme(theme);

        Mockito.when(mealService.findById(1L)).thenReturn(Optional.of(meal));

        mockMvc.perform(get("/api/meals/1/ingredients")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Eggs")));
    }

    @Test
    void getIngredientsForMeal_returnsHtmlForBrowser() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Eggs");
        ingredient.setQuantity("2");

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Omelette");
        meal.setIngredients(List.of(ingredient));
        DayTheme theme = new DayTheme("Monday", "Breakfast Bonanza");
        meal.setDayTheme(theme);

        Mockito.when(mealService.findById(1L)).thenReturn(Optional.of(meal));

        mockMvc.perform(get("/api/meals/1/ingredients")
                .header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Ingredients for Omelette")));
    }

    @Test
    void getIngredientsForMeal_returnsNotFound() throws Exception {
        Mockito.when(mealService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/meals/999/ingredients")
                .header("User-Agent", "curl"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getInstructionsForMeal_returnsJson() throws Exception {
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Omelette");
        meal.setInstructions("Beat eggs and cook.");
        DayTheme theme = new DayTheme("Monday", "Breakfast Bonanza");
        meal.setDayTheme(theme);

        Mockito.when(mealService.findById(1L)).thenReturn(Optional.of(meal));

        mockMvc.perform(get("/api/meals/1/instructions")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Beat eggs and cook.")));
    }

    @Test
    void getInstructionsForMeal_returnsHtmlForBrowser() throws Exception {
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Omelette");
        meal.setInstructions("Beat eggs and cook.");
        DayTheme theme = new DayTheme("Monday", "Breakfast Bonanza");
        meal.setDayTheme(theme);

        Mockito.when(mealService.findById(1L)).thenReturn(Optional.of(meal));

        mockMvc.perform(get("/api/meals/1/instructions")
                .header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Instructions for Omelette")));
    }

    @Test
    void getInstructionsForMeal_returnsNotFound() throws Exception {
        Mockito.when(mealService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/meals/999/instructions")
                .header("User-Agent", "curl"))
                .andExpect(status().isNotFound());
    }
}