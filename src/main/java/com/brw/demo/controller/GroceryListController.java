package com.brw.demo.controller;

import com.brw.demo.dto.GroceryListItemDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.service.DayThemeService;
import com.brw.demo.service.MealService;
import com.brw.demo.util.HtmlResponseUtil;
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

    private boolean isBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && userAgent.toLowerCase().contains("mozilla");
    }

    // GET /api/grocery-list/weekly
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyGroceryList(HttpServletRequest request) {
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
        if (isBrowser(request)) {
            StringBuilder html = new StringBuilder();
            html.append(HtmlResponseUtil.htmlHeader("Weekly Grocery List"));
            html.append(HtmlResponseUtil.buttonGroup());
            html.append(HtmlResponseUtil.breadcrumb("Grocery List"));
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
            html.append(HtmlResponseUtil.htmlFooter());
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
        }
        List<GroceryListItemDTO> allItems = new ArrayList<>();
        STAPLES.values().forEach(allItems::addAll);
        allItems.addAll(groceryList);
        return ResponseEntity.ok(allItems);
    }
}
