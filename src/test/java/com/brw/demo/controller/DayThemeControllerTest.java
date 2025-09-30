package com.brw.demo.controller;

import com.brw.demo.dto.DayThemeResponseDTO;
import com.brw.demo.dto.DayThemeSummaryDTO;
import com.brw.demo.dto.MealDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.service.DayThemeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DayThemeController.class)
class DayThemeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DayThemeService dayThemeService;

    @Test
    void getAllThemes_returnsThemesJson() throws Exception {
        List<DayThemeSummaryDTO> themes = Arrays.asList(
                new DayThemeSummaryDTO("Monday", "Breakfast Bonanza"),
                new DayThemeSummaryDTO("Tuesday", "Taco Tuesday")
        );
        when(dayThemeService.findAll()).thenReturn(Arrays.asList(
                new DayTheme("Monday", "Breakfast Bonanza"),
                new DayTheme("Tuesday", "Taco Tuesday")
        ));

        mockMvc.perform(get("/api/themes")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMealsForDay_returnsMealsJson() throws Exception {
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Pancakes");
        DayTheme dayTheme = new DayTheme("Monday", "Breakfast Bonanza");
        dayTheme.setMeals(Arrays.asList(meal));
        when(dayThemeService.findByName("Monday")).thenReturn(Optional.of(dayTheme));

        mockMvc.perform(get("/api/themes/Monday")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMealsForDay_returnsNotFound() throws Exception {
        when(dayThemeService.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/themes/NonexistentDay")
                .header("User-Agent", "curl"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMenuPlan_returnsMenuPlanJson() throws Exception {
        when(dayThemeService.findByName(anyString())).thenReturn(Optional.of(new DayTheme("Monday", "Breakfast Bonanza")));

        mockMvc.perform(get("/api/themes/menu-plan")
                .header("User-Agent", "curl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllThemes_returnsHtmlForBrowser() throws Exception {
        when(dayThemeService.findAll()).thenReturn(Arrays.asList(
                new DayTheme("Monday", "Breakfast Bonanza"),
                new DayTheme("Tuesday", "Taco Tuesday")
        ));

        mockMvc.perform(get("/api/themes")
                .header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;html"));
    }

@Test
void createHtmlResponse_returnsCorrectHtmlForDayThemeResponseDTO() {
        // Arrange
        MealDTO meal1 = new MealDTO(1L, "Pancakes");
        MealDTO meal2 = new MealDTO(2L, "Omelette");
        DayThemeResponseDTO dto = new DayThemeResponseDTO("Monday", "Breakfast Bonanza", Arrays.asList(meal1, meal2));

        // Act
        // We need to access the private method via reflection
        DayThemeController controller = new DayThemeController(dayThemeService, new com.fasterxml.jackson.databind.ObjectMapper());
        java.lang.reflect.Method method;
        ResponseEntity<String> response;
        try {
                method = DayThemeController.class.getDeclaredMethod("createHtmlResponse", DayThemeResponseDTO.class);
                method.setAccessible(true);
                response = (ResponseEntity<String>) method.invoke(controller, dto);
        } catch (Exception e) {
                throw new RuntimeException(e);
        }

        // Assert
        String body = response.getBody();
        org.assertj.core.api.Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
        org.assertj.core.api.Assertions.assertThat(response.getHeaders().getFirst("Content-Type")).isEqualTo("text/html");
        org.assertj.core.api.Assertions.assertThat(body).contains("Monday - What's For Dinner");
        org.assertj.core.api.Assertions.assertThat(body).contains("Breakfast Bonanza");
        org.assertj.core.api.Assertions.assertThat(body).contains("Pancakes");
        org.assertj.core.api.Assertions.assertThat(body).contains("Omelette");
        org.assertj.core.api.Assertions.assertThat(body).contains("<table");
        org.assertj.core.api.Assertions.assertThat(body).contains("<a href='/api/meals/1'>Pancakes</a>");
        org.assertj.core.api.Assertions.assertThat(body).contains("<a href='/api/meals/2'>Omelette</a>");
}
}
