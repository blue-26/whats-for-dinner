package com.brw.demo.util;

public class HtmlResponseUtil {
    public static String htmlHeader(String title) {
        return "<!DOCTYPE html><html><head><title>" + title + "</title>"
            + "<style>body{font-family:sans-serif;background:#f8f9fa;margin:0;padding:0;}h1{background:#343a40;color:#fff;padding:1em 2em;margin:0;}main{padding:2em;}table{width:100%;border-collapse:collapse;margin-top:1em;}th,td{padding:0.75em 1em;border-bottom:1px solid #dee2e6;}tr:hover{background:#f1f3f5;}a{color:#007bff;text-decoration:none;}a:hover{text-decoration:underline;} .card{background:#fff;border-radius:8px;box-shadow:0 2px 8px #0001;padding:1.5em;margin-bottom:1em;}</style>"
            + "</head><body>"
            + "<h1><a href='/api/themes' style='color:#fff;text-decoration:none;'>What's For Dinner</a></h1><main>";
    }

    public static String htmlFooter() {
        return "</main></body></html>";
    }
}
