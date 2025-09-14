package com.brw.demo.controller;

import com.brw.demo.model.Meal;
import com.brw.demo.service.MealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.brw.demo.util.HtmlResponseUtil;

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

    private ResponseEntity<String> createHtmlResponse(Object data) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            String markdown = "```json\n" + json + "\n```";

            Parser parser = Parser.builder().build();
            Node document = parser.parse(markdown);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String html = renderer.render(document);

            String fullHtml = "<!DOCTYPE html><html><head><title>API Response</title>" +
                              "<style>body { font-family: sans-serif; } pre { background-color: #f4f4f4; padding: 1em; border-radius: 5px; }</style>" +
                              "</head><body>" + html + "</body></html>";

            return ResponseEntity.ok().header("Content-Type", "text/html").body(fullHtml);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<String> createHtmlResponse(Meal meal) {
        StringBuilder html = new StringBuilder();
        html.append(HtmlResponseUtil.htmlHeader(meal.getName() + " - What's For Dinner"));
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
