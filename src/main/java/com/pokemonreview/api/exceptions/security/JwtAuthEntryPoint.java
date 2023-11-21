package com.pokemonreview.api.exceptions.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.exceptions.ErrorObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
             throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401

        ObjectMapper mapper = new ObjectMapper();
        // Create response content
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        errorObject.setMessage("요청된 리소스에 대한 유효한 인증 자격 증명이 실패 하였습니다.!");

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(errorObject);
        response.getWriter().write(json);
    }
}