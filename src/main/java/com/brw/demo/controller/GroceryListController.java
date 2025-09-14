package com.brw.demo.controller;

import com.brw.demo.dto.GroceryListItemDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.service.DayThemeService;
import com.brw.demo.service.MealService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grocery-list")
public class GroceryListController {
    private final DayThemeService dayThemeService;
    private final MealService mealService;

    public GroceryListController(DayThemeService dayThemeService, MealService mealService) {
        this.dayThemeService = dayThemeService;
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

    private static final Map<String, List<GroceryListItemDTO>> STAPLES = Map.of(
        "Pantry", List.of(
            new GroceryListItemDTO("Bread", "1 loaf"),
            new GroceryListItemDTO("Bagels", "1 pack"),
            new GroceryListItemDTO("Cereal", "1 box"),
            new GroceryListItemDTO("Chips", "1 bag")
        ),
        "Dairy", List.of(
            new GroceryListItemDTO("Milk", "1 gallon"),
            new GroceryListItemDTO("Butter", "1 stick"),
            new GroceryListItemDTO("Eggs", "1 dozen"),
            new GroceryListItemDTO("Cheese", "8 oz"),
            new GroceryListItemDTO("Cream Cheese", "1 tub"),
            new GroceryListItemDTO("Sour Cream", "1 tub")
        ),
        "Produce", List.of(
            new GroceryListItemDTO("Strawberries", "1 lb"),
            new GroceryListItemDTO("Bananas", "6"),
            new GroceryListItemDTO("Apples", "4"),
            new GroceryListItemDTO("Tomatoes", "3"),
            new GroceryListItemDTO("Lettuce", "1 head")
        ),
        "Deli/Other", List.of(
            new GroceryListItemDTO("Lunch Meat", "1 lb"),
            new GroceryListItemDTO("Tofu", "1 block")
        )
    );

    // GET /api/grocery-list/weekly
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyGroceryList(HttpServletRequest request) {
        // Days in order
        List<String> daysOfWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        Map<String, Integer> ingredientMap = new HashMap<>();
        for (String day : daysOfWeek) {
            Optional<DayTheme> themeOpt = dayThemeService.findByName(day);
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
            html.append("<style>body{font-family:sans-serif;background:#f8f9fa;margin:0;padding:0;}h1{background:#343a40;color:#fff;padding:1em 2em;margin:0;}main{padding:2em;}table{width:100%;border-collapse:collapse;margin-top:1em;}th,td{padding:0.75em 1em;border-bottom:1px solid #dee2e6;}tr:hover{background:#f1f3f5;}a{color:#007bff;text-decoration:none;}a:hover{text-decoration:underline;} .card{background:#fff;border-radius:8px;box-shadow:0 2px 8px #0001;padding:1.5em;margin-bottom:1em;} .cat{font-weight:bold;background:#e9ecef;}</style>");
            html.append("</head><body>");
            html.append("<h1><a href='/api/themes/menu-plan' style='color:#fff;text-decoration:none;'>Weekly Menu Plan</a></h1><main>");
            html.append("<h2>Weekly Staples</h2>");
            html.append("<table><thead><tr><th>Category</th><th>Item</th><th>Quantity</th></tr></thead><tbody>");
            for (var entry : STAPLES.entrySet()) {
                String category = entry.getKey();
                for (GroceryListItemDTO item : entry.getValue()) {
                    html.append("<tr><td class='cat'>").append(category).append("</td><td>").append(item.getName()).append("</td><td>").append(item.getQuantity()).append("</td></tr>");
                }
            }
            html.append("</tbody></table>");
            html.append("<h2 style='margin-top:2em;'>Menu Plan Ingredients</h2>");
            html.append("<table><thead><tr><th>Ingredient</th><th>Quantity</th></tr></thead><tbody>");
            for (GroceryListItemDTO item : groceryList) {
                html.append("<tr><td>").append(item.getName()).append("</td><td>").append(item.getQuantity()).append("</td></tr>");
            }
            html.append("</tbody></table>");
            html.append("<p style='margin-top:2em;'><a href='/api/themes/menu-plan'>&larr; Back to Menu Plan</a></p>");
            html.append("</main></body></html>");
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
        }
        // For JSON, combine staples and menu plan ingredients
        List<GroceryListItemDTO> allItems = new ArrayList<>();
        STAPLES.values().forEach(allItems::addAll);
        allItems.addAll(groceryList);
        return ResponseEntity.ok(allItems);
    }
}
