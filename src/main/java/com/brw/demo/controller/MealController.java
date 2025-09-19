package com.brw.demo.controller;

import com.brw.demo.model.Meal;
import com.brw.demo.service.MealService;
import com.brw.demo.util.HtmlResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final ObjectMapper objectMapper;

    public MealController(MealService mealService, ObjectMapper objectMapper) {
        this.mealService = mealService;
        this.objectMapper = objectMapper;
    }

    private boolean isBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && userAgent.toLowerCase().contains("mozilla");
    }

    private ResponseEntity<String> createHtmlResponse(Meal meal) {
        StringBuilder html = new StringBuilder();
        html.append(HtmlResponseUtil.htmlHeader(meal.getName() + " - What's For Dinner"));
        html.append(HtmlResponseUtil.buttonGroup());
        html.append(HtmlResponseUtil.breadcrumb("Daily Options", meal.getDayTheme().getName(), meal.getName()));
        html.append("<div class='card'><h2>").append(meal.getName()).append("</h2>");
        html.append("<h3>Ingredients</h3><ul>");
        for (var ingredient : meal.getIngredients()) {
            html.append("<li>")
                .append(ingredient.getName())
                .append(": ")
                .append(ingredient.getQuantity())
                .append("</li>");
        }
        html.append("</ul><h3>Instructions</h3><p>").append(meal.getInstructions()).append("</p></div>");
        html.append("<p style='margin-top:2em;'><a href='/api/themes/" + meal.getDayTheme().getName() + "'>&larr; Back to "+ meal.getDayTheme().getName() +"</a></p>");
        html.append(HtmlResponseUtil.htmlFooter());
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMealById(@PathVariable Long id, HttpServletRequest request) {
        return mealService.findById(id)
                .map(meal -> {
                    if (isBrowser(request)) {
                        return createHtmlResponse(meal);
                    }
                    return ResponseEntity.ok(meal);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
