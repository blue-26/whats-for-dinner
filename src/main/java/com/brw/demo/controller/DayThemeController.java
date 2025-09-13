
package com.brw.demo.controller;

import com.brw.demo.dto.MealDTO;
import com.brw.demo.service.DayThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/themes")
public class DayThemeController {

    private final DayThemeService dayThemeService;

    public DayThemeController(DayThemeService dayThemeService) {
        this.dayThemeService = dayThemeService;
    }

    @GetMapping("")
    public ResponseEntity<List<String>> getAllThemes() {
        List<String> themeNames = dayThemeService.findAll().stream()
                .map(theme -> theme.getName())
                .collect(Collectors.toList());
        return ResponseEntity.ok(themeNames);
    }

    @GetMapping("/{dayName}/meals")
    public ResponseEntity<List<MealDTO>> getMealsForDay(@PathVariable String dayName) {
        return dayThemeService.findByName(dayName)
                .map(dayTheme -> {
                    List<MealDTO> mealDTOs = dayTheme.getMeals().stream()
                            .map(meal -> new MealDTO(meal.getId(), meal.getName()))
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(mealDTOs);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
