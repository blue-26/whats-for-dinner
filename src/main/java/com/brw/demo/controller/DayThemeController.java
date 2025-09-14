package com.brw.demo.controller;

import com.brw.demo.dto.DayThemeResponseDTO;
import com.brw.demo.dto.DayThemeSummaryDTO;
import com.brw.demo.dto.MealDTO;
import com.brw.demo.dto.MenuPlanEntryDTO;
import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.service.DayThemeService;
import com.brw.demo.util.HtmlResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/themes")
public class DayThemeController {

    private final DayThemeService dayThemeService;
    private final ObjectMapper objectMapper;

    public DayThemeController(DayThemeService dayThemeService, ObjectMapper objectMapper) {
        this.dayThemeService = dayThemeService;
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

    private ResponseEntity<String> createHtmlResponse(List<DayThemeSummaryDTO> themes) {
        StringBuilder html = new StringBuilder();
        html.append(HtmlResponseUtil.htmlHeader("What's For Dinner - Themes"));
        html.append("<h2>Days &amp; Themes</h2>");
        html.append("<table><thead><tr><th>Day</th><th>Theme</th></tr></thead><tbody>");
        for (DayThemeSummaryDTO t : themes) {
            html.append("<tr><td><a href='/api/themes/").append(t.getDay()).append("'>").append(t.getDay()).append("</a></td><td>").append(t.getThemeDescription()).append("</td></tr>");
        }
        html.append("</tbody></table>");
        html.append(HtmlResponseUtil.htmlFooter());
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
    }

    private ResponseEntity<String> createHtmlResponse(DayThemeResponseDTO dto) {
        StringBuilder html = new StringBuilder();
        html.append(HtmlResponseUtil.htmlHeader(dto.getDay() + " - What's For Dinner"));
        html.append("<div class='card'><h2>").append(dto.getDay()).append("</h2><p><strong>Theme:</strong> ").append(dto.getTheme()).append("</p></div>");
        html.append("<h3>Meals</h3><table><thead><tr><th>Name</th></tr></thead><tbody>");
        for (MealDTO meal : dto.getMeals()) {
            html.append("<tr><td><a href='/api/meals/").append(meal.getId()).append("'>").append(meal.getName()).append("</a></td></tr>");
        }
        html.append("</tbody></table>");
        html.append("<p style='margin-top:2em;'><a href='/api/themes'>&larr; Back to all days</a></p>");
        html.append(HtmlResponseUtil.htmlFooter());
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
    }

    @GetMapping("")
    public ResponseEntity<?> getAllThemes(HttpServletRequest request) {
        var themes = dayThemeService.findAll().stream()
                .map(theme -> new DayThemeSummaryDTO(theme.getName(), theme.getThemeDescription()))
                .collect(Collectors.toList());

        if (isBrowser(request)) {
            return createHtmlResponse(themes);
        }
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{dayName}")
    public ResponseEntity<?> getMealsForDay(@PathVariable String dayName, HttpServletRequest request) {
        return dayThemeService.findByName(dayName)
                .map(dayTheme -> {
                    List<MealDTO> mealDTOs = dayTheme.getMeals().stream()
                            .map(meal -> new MealDTO(meal.getId(), meal.getName()))
                            .collect(Collectors.toList());

                    DayThemeResponseDTO responseDTO = new DayThemeResponseDTO(dayTheme.getName(), dayTheme.getThemeDescription(), mealDTOs);
                    
                    if (isBrowser(request)) {
                        return createHtmlResponse(responseDTO);
                    }
                    return ResponseEntity.ok(responseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu-plan")
    public ResponseEntity<?> getMenuPlan(HttpServletRequest request) {
        // Days in order
        List<String> daysOfWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        List<MenuPlanEntryDTO> plan = new ArrayList<>();
        for (String day : daysOfWeek) {
            Optional<DayTheme> themeOpt = dayThemeService.findByName(day);
            if (themeOpt.isPresent()) {
                DayTheme theme = themeOpt.get();
                List<Meal> meals = theme.getMeals();
                MealDTO chosenMeal = null;
                if (meals != null && !meals.isEmpty()) {
                    Meal meal = meals.get(new Random().nextInt(meals.size()));
                    chosenMeal = new MealDTO(meal.getId(), meal.getName());
                }
                plan.add(new MenuPlanEntryDTO(day, theme.getThemeDescription(), chosenMeal));
            } else {
                plan.add(new MenuPlanEntryDTO(day, "No theme", null));
            }
        }
        if (isBrowser(request)) {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><title>Weekly Menu Plan</title>");
            html.append("<style>body{font-family:sans-serif;background:#f8f9fa;margin:0;padding:0;}h1{background:#343a40;color:#fff;padding:1em 2em;margin:0;}main{padding:2em;}table{width:100%;border-collapse:collapse;margin-top:1em;}th,td{padding:0.75em 1em;border-bottom:1px solid #dee2e6;}tr:hover{background:#f1f3f5;}a{color:#007bff;text-decoration:none;}a:hover{text-decoration:underline;} .card{background:#fff;border-radius:8px;box-shadow:0 2px 8px #0001;padding:1.5em;margin-bottom:1em;}</style>");
            html.append("</head><body>");
            html.append("<h1><a href='/api/themes' style='color:#fff;text-decoration:none;'>What's For Dinner</a></h1><main>");
            html.append("<a href='/api/grocery-list/weekly' style='display:inline-block;margin-bottom:2em;padding:1em 2em;background:#28a745;color:#fff;text-decoration:none;border-radius:6px;font-size:1.1em;'>🛒 Generate Grocery List</a>");
            html.append("<h2>Weekly Menu Plan</h2>");
            html.append("<table><thead><tr><th>Day</th><th>Theme</th><th>Meal</th></tr></thead><tbody>");
            for (MenuPlanEntryDTO entry : plan) {
                html.append("<tr><td>").append(entry.getDay()).append("</td><td>").append(entry.getTheme()).append("</td><td>");
                if (entry.getMeal() != null) {
                    html.append("<a href='/api/meals/").append(entry.getMeal().getId()).append("'>").append(entry.getMeal().getName()).append("</a>");
                } else {
                    html.append("-");
                }
                html.append("</td></tr>");
            }
            html.append("</tbody></table>");
            html.append("<p style='margin-top:2em;'><a href='/api/themes'>&larr; Back to all days</a></p>");
            html.append("</main></body></html>");
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
        }
        return ResponseEntity.ok(plan);
    }
}