package com.brw.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LandingController {
    @GetMapping("")
    public ResponseEntity<String> landing() {
        String html = "<!DOCTYPE html><html><head><title>What's For Dinner?</title>"
            + "<style>body{font-family:sans-serif;background:#f8f9fa;margin:0;padding:0;}h1{background:#343a40;color:#fff;padding:2em 1em 1em 1em;margin:0;}main{padding:2em;text-align:center;}a{display:inline-block;margin-top:2em;padding:1em 2em;background:#007bff;color:#fff;text-decoration:none;border-radius:6px;font-size:1.2em;}a:hover{background:#0056b3;}</style>"
            + "</head><body>"
            + "<h1>What's For Dinner?</h1>"
            + "<main>"
            + "<p style='font-size:1.5em;'>The question is finally answered!</p>"
            + "<a href='/api/themes'>See the Menu</a>"
            + "</main></body></html>";
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
    }
}
