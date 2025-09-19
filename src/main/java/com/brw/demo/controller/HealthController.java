package com.brw.demo.controller;

import com.brw.demo.util.HtmlResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> health(HttpServletRequest request) {
        if (request.getHeader("User-Agent") != null && request.getHeader("User-Agent").toLowerCase().contains("mozilla")) {
            StringBuilder html = new StringBuilder();
            html.append(HtmlResponseUtil.htmlHeader("Health Check"));
            html.append(HtmlResponseUtil.buttonGroup());
            html.append(HtmlResponseUtil.breadcrumb("Health"));
            html.append("<div class='card'><h2>Health Status</h2><p>OK</p></div>");
            html.append(HtmlResponseUtil.htmlFooter());
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
        }
        return ResponseEntity.ok("OK");
    }
}
