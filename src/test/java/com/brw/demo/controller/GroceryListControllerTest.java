package com.brw.demo.controller;

import com.brw.demo.dto.GroceryListItemDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.model.Ingredient;
import com.brw.demo.service.DayThemeService;
import com.brw.demo.service.MealService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroceryListController.class)
class GroceryListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DayThemeService dayThemeService;

    @MockBean
    private MealService mealService;

    @Test
    void getGroceryList_returnsCombinedIngredients() throws Exception {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Eggs");
        ingredient1.setQuantity("2");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Milk");
        ingredient2.setQuantity("1");

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setIngredients(Arrays.asList(ingredient1, ingredient2));

        when(mealService.findById(1L)).thenReturn(Optional.of(meal));
        when(mealService.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/grocery-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1,2]"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Eggs")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Milk")));
    }

    @Test
    void getWeeklyGroceryList_returnsHtmlForBrowser() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredient.setQuantity("2");

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setIngredients(List.of(ingredient));

        DayTheme theme = new DayTheme("Monday", "Breakfast Bonanza");
        theme.setMeals(List.of(meal));

        when(dayThemeService.findByName(anyString())).thenReturn(Optional.of(theme));

        mockMvc.perform(get("/api/grocery-list/weekly")
                .header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Weekly Staples")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Menu Plan Ingredients")));
    }

    @Test
    void getWeeklyGroceryList_returnsJsonForApi() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredient.setQuantity("2");

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setIngredients(List.of(ingredient));

        DayTheme theme = new DayTheme("Monday", "Breakfast Bonanza");
        theme.setMeals(List.of(meal));

        when(dayThemeService.findByName(anyString())).thenReturn(Optional.of(theme));

        mockMvc.perform(get("/api/grocery-list/weekly")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Eggs")));
    }

    @Test
    void getWeeklyGroceryList_handlesMissingTheme() throws Exception {
        when(dayThemeService.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/grocery-list/weekly")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}