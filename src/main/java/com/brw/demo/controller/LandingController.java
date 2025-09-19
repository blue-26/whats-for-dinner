package com.brw.demo.controller;

import com.brw.demo.util.HtmlResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LandingController {
    @GetMapping("")
    public ResponseEntity<String> landing() {
        StringBuilder html = new StringBuilder();
        html.append(HtmlResponseUtil.htmlHeader("What's For Dinner?"));
        html.append(HtmlResponseUtil.buttonGroup());
        html.append(HtmlResponseUtil.breadcrumb("Home"));
        html.append("<main style='text-align:center;'>");
        html.append("<p style='font-size:1.5em;'>The question is finally answered!</p>");
        html.append("</main>");
        html.append(HtmlResponseUtil.htmlFooter());
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
    }
}
