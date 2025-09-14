package com.brw.demo.controller;

import com.brw.demo.dto.DayThemeResponseDTO;
import com.brw.demo.dto.DayThemeSummaryDTO;
import com.brw.demo.dto.MealDTO;
import com.brw.demo.service.DayThemeService;
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

import java.util.List;
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
}
