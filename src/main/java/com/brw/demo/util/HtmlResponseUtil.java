package com.brw.demo.util;

public class HtmlResponseUtil {
    public static String htmlHeader(String title) {
        return "<!DOCTYPE html><html><head><title>" + title + "</title>"
            + "<style>body{font-family:sans-serif;background:#f8f9fa;margin:0;padding:0;}h1{background:#343a40;color:#fff;padding:1em 2em;margin:0;}main{padding:2em;}table{width:100%;border-collapse:collapse;margin-top:1em;}th,td{padding:0.75em 1em;border-bottom:1px solid #dee2e6;}tr:hover{background:#f1f3f5;}a{color:#007bff;text-decoration:none;}a:hover{text-decoration:underline;} .card{background:#fff;border-radius:8px;box-shadow:0 2px 8px #0001;padding:1.5em;margin-bottom:1em;} .btn{display:inline-block;padding:10px 20px;margin:0 10px;background:#007bff;color:#fff;border-radius:5px;text-decoration:none;font-weight:bold;} .breadcrumb{margin-bottom:20px;} .breadcrumb a{background:#eee;padding:6px 12px;border-radius:4px;text-decoration:none;color:#333;margin-right:8px;} .breadcrumb span{margin-right:8px;}</style>"
            + "</head><body>"
            + "<h1><a href='/' style='color:#fff;text-decoration:none;'>What's For Dinner</a></h1><main>";
    }

    public static String htmlFooter() {
        return "</main></body></html>";
    }

    public static String buttonGroup() {
        return "<div style='margin-bottom:20px;text-align:center;'>"
            + "<a href='/api/themes/menu-plan' class='btn'>Menu Plan</a>"
            + "<a href='/api/grocery-list/weekly' class='btn'>Grocery List</a>"
            + "<a href='/api/themes' class='btn'>Daily Options</a>"
            + "</div>";
    }

    public static String breadcrumb(String... crumbs) {
        StringBuilder html = new StringBuilder("<div class='breadcrumb'>");
        html.append("<a href='/'>Home</a>");
        for (String crumb : crumbs) {
            html.append("<span>&gt;</span>");
            html.append("<a href='#'>").append(crumb).append("</a>");
        }
        html.append("</div>");
        return html.toString();
    }
}
