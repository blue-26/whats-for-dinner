package com.brw.demo.controller;

import com.brw.demo.dto.IngredientDTO;
import com.brw.demo.dto.MealDTO;
import com.brw.demo.service.MealService;
import com.brw.demo.util.HtmlResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class MenuPlannerController {

    private final MealService mealService;

    public MenuPlannerController(MealService mealService) {
        this.mealService = mealService;
    }

    private boolean isBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && userAgent.toLowerCase().contains("mozilla");
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
    public ResponseEntity<?> getIngredientsForMeal(@PathVariable Long mealId, HttpServletRequest request) {
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
                    if (isBrowser(request)) {
                        StringBuilder html = new StringBuilder();
                        html.append(HtmlResponseUtil.htmlHeader("Ingredients for " + meal.getName()));
                        html.append(HtmlResponseUtil.buttonGroup());
                        html.append(HtmlResponseUtil.breadcrumb("Daily Options", meal.getDayTheme().getName(), meal.getName(), "Ingredients"));
                        html.append("<div class='card'><h2>Ingredients for ").append(meal.getName()).append("</h2><ul>");
                        for (IngredientDTO dto : ingredientDTOs) {
                            html.append("<li>").append(dto.getName()).append(": ").append(dto.getQuantity()).append("</li>");
                        }
                        html.append("</ul></div>");
                        html.append(HtmlResponseUtil.htmlFooter());
                        return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
                    }
                    return ResponseEntity.ok(ingredientDTOs);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/meals/{mealId}/instructions
    @GetMapping("/meals/{mealId}/instructions")
    public ResponseEntity<?> getInstructionsForMeal(@PathVariable Long mealId, HttpServletRequest request) {
        return mealService.findById(mealId)
                .map(meal -> {
                    if (isBrowser(request)) {
                        StringBuilder html = new StringBuilder();
                        html.append(HtmlResponseUtil.htmlHeader("Instructions for " + meal.getName()));
                        html.append(HtmlResponseUtil.buttonGroup());
                        html.append(HtmlResponseUtil.breadcrumb("Daily Options", meal.getDayTheme().getName(), meal.getName(), "Instructions"));
                        html.append("<div class='card'><h2>Instructions for ").append(meal.getName()).append("</h2><p>").append(meal.getInstructions()).append("</p></div>");
                        html.append(HtmlResponseUtil.htmlFooter());
                        return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
                    }
                    // FIX: Return JSON content type for API clients
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("\"" + meal.getInstructions() + "\"");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

