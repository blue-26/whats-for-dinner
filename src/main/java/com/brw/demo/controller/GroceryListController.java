package com.brw.demo.controller;

import com.brw.demo.dto.GroceryListItemDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.service.MealService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grocery-list")
public class GroceryListController {
    private final MealService mealService;

    public GroceryListController(MealService mealService) {
        this.mealService = mealService;
    }

    // POST /api/grocery-list
    @PostMapping
    public ResponseEntity<List<GroceryListItemDTO>> getGroceryList(@RequestBody List<Long> mealIds) {
        Map<String, Integer> ingredientMap = new HashMap<>();
        for (Long mealId : mealIds) {
            Optional<Meal> mealOpt = mealService.findById(mealId);
            mealOpt.ifPresent(meal -> {
                meal.getIngredients().forEach(ingredient -> {
                    String name = ingredient.getName();
                    int qty = 1;
                    try {
                        qty = Integer.parseInt(ingredient.getQuantity().replaceAll("[^0-9]", ""));
                    } catch (Exception ignored) {}
                    ingredientMap.put(name, ingredientMap.getOrDefault(name, 0) + qty);
                });
            });
        }
        List<GroceryListItemDTO> groceryList = ingredientMap.entrySet().stream()
                .map(e -> new GroceryListItemDTO(e.getKey(), String.valueOf(e.getValue())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(groceryList);
    }

    // GET /api/grocery-list/weekly
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyGroceryList(HttpServletRequest request) {
        // Days in order
        List<String> daysOfWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        Map<String, Integer> ingredientMap = new HashMap<>();
        for (String day : daysOfWeek) {
            Optional<DayTheme> themeOpt = mealService.getDayThemeRepository().findByName(day);
            if (themeOpt.isPresent()) {
                DayTheme theme = themeOpt.get();
                List<Meal> meals = theme.getMeals();
                if (meals != null && !meals.isEmpty()) {
                    Meal meal = meals.get(new Random().nextInt(meals.size()));
                    meal.getIngredients().forEach(ingredient -> {
                        String name = ingredient.getName();
                        int qty = 1;
                        try {
                            qty = Integer.parseInt(ingredient.getQuantity().replaceAll("[^0-9]", ""));
                        } catch (Exception ignored) {}
                        ingredientMap.put(name, ingredientMap.getOrDefault(name, 0) + qty);
                    });
                }
            }
        }
        List<GroceryListItemDTO> groceryList = ingredientMap.entrySet().stream()
                .map(e -> new GroceryListItemDTO(e.getKey(), String.valueOf(e.getValue())))
                .collect(Collectors.toList());
        if (request.getHeader("User-Agent") != null && request.getHeader("User-Agent").toLowerCase().contains("mozilla")) {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><title>Weekly Grocery List</title>");
            html.append("<style>body{font-family:sans-serif;background:#f8f9fa;margin:0;padding:0;}h1{background:#343a40;color:#fff;padding:1em 2em;margin:0;}main{padding:2em;}table{width:100%;border-collapse:collapse;margin-top:1em;}th,td{padding:0.75em 1em;border-bottom:1px solid #dee2e6;}tr:hover{background:#f1f3f5;}a{color:#007bff;text-decoration:none;}a:hover{text-decoration:underline;} .card{background:#fff;border-radius:8px;box-shadow:0 2px 8px #0001;padding:1.5em;margin-bottom:1em;}</style>");
            html.append("</head><body>");
            html.append("<h1><a href='/api/themes/menu-plan' style='color:#fff;text-decoration:none;'>Weekly Menu Plan</a></h1><main>");
            html.append("<h2>Grocery List</h2>");
            html.append("<table><thead><tr><th>Ingredient</th><th>Quantity</th></tr></thead><tbody>");
            for (GroceryListItemDTO item : groceryList) {
                html.append("<tr><td>").append(item.getName()).append("</td><td>").append(item.getQuantity()).append("</td></tr>");
            }
            html.append("</tbody></table>");
            html.append("<p style='margin-top:2em;'><a href='/api/themes/menu-plan'>&larr; Back to Menu Plan</a></p>");
            html.append("</main></body></html>");
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
        }
        return ResponseEntity.ok(groceryList);
    }
}
