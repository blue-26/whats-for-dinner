package com.brw.demo.controller;

import com.brw.demo.model.Meal;
import com.brw.demo.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    /**
     * Endpoint to create a hardcoded Spaghetti meal.
     * This is useful for testing and demonstration.
     * @return The created Meal object.
     */
    @PostMapping("/create-spaghetti")
    public ResponseEntity<Meal> createSpaghetti() {
        Meal createdMeal = mealService.createSpaghettiMeal();
        return ResponseEntity.ok(createdMeal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        return mealService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
