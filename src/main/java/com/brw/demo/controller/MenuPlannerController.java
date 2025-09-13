package com.brw.demo.controller;

import com.brw.demo.dto.IngredientDTO;
import com.brw.demo.dto.MealDTO;
import com.brw.demo.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MenuPlannerController {

    private final MealService mealService;

    public MenuPlannerController(MealService mealService) {
        this.mealService = mealService;
    }

    // POST /api/meals
    @PostMapping("/meals")
    public MealDTO addMeal(@RequestBody MealDTO mealDTO) {
        // TODO: Implement service call
        return null;
    }

    // PUT /api/meals/{mealId}
    @PutMapping("/meals/{mealId}")
    public MealDTO updateMeal(@PathVariable Long mealId, @RequestBody MealDTO mealDTO) {
        // TODO: Implement service call
        return null;
    }

    // DELETE /api/meals/{mealId}
    @DeleteMapping("/meals/{mealId}")
    public void deleteMeal(@PathVariable Long mealId) {
        // TODO: Implement service call
    }

    // GET /api/meals/{mealId}/ingredients
    @GetMapping("/meals/{mealId}/ingredients")
    public ResponseEntity<List<IngredientDTO>> getIngredientsForMeal(@PathVariable Long mealId) {
        return mealService.findById(mealId)
                .map(meal -> {
                    List<IngredientDTO> ingredientDTOs = meal.getIngredients().stream()
                            .map(ingredient -> {
                                IngredientDTO dto = new IngredientDTO();
                                dto.setId(ingredient.getId());
                                dto.setName(ingredient.getName());
                                dto.setQuantity(ingredient.getQuantity());
                                return dto;
                            })
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(ingredientDTOs);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/meals/{mealId}/instructions
    @GetMapping("/meals/{mealId}/instructions")
    public ResponseEntity<String> getInstructionsForMeal(@PathVariable Long mealId) {
        return mealService.findById(mealId)
                .map(meal -> ResponseEntity.ok(meal.getInstructions()))
                .orElse(ResponseEntity.notFound().build());
    }
}

