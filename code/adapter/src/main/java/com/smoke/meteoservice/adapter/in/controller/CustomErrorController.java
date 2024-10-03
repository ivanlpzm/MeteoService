package com.smoke.meteoservice.adapter.in.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null && statusCode.equals(HTTP_NOT_FOUND)) {
            return "error/404";
        }
        return "error/error";
    }
}
