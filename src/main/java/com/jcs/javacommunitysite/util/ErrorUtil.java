package com.jcs.javacommunitysite.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

public class ErrorUtil {
    public static String createErrorToast(HttpServletResponse resp, Model model, String message) {
        resp.setHeader("HX-Reswap", "beforeend");
        resp.setHeader("HX-Retarget", "body");
        resp.setStatus(500);
        model.addAttribute("toastMsg", message);
        return "components/errorToast";
    }
}
