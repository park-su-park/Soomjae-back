package com.parksupark.soomjae.server.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        logRequestHeader(request);

        // 다음 filter 실행
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logRequestBody(wrappedRequest);

        logResponseHeader(wrappedResponse);
        logResponseBody(wrappedResponse);

        // 실제 응답 전송 -> doFilter에 wrappedResponse를 넘겼으므로 response는 비어있음
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestHeader(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
            headers.put(headerName, request.getHeader(headerName))
        );
        try {
            String jsonHeaders = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(headers);
            log.info("Request Headers:\n{}", jsonHeaders);
        } catch (JsonProcessingException e) {
            log.info("Request Headers: (failed to format as JSON)");
        }
    }

    private void logRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();

        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            String formattedBody = formatJson(body);
            log.info("Request Body:\n{}", formattedBody);
        }
    }

    private void logResponseHeader(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        for (String headerName : response.getHeaderNames()) {
            headers.put(headerName, response.getHeader(headerName));
        }
        try {
            String jsonHeaders = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(headers);
            log.info("Response Headers:\n{}", jsonHeaders);
        } catch (JsonProcessingException e) {
            log.info("Response Headers: (failed to format as JSON)");
        }
    }

    private void logResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            String formattedBody = formatJson(body);
            log.info("Response Body:\n{}", formattedBody);
        }
    }

    private String formatJson(String jsonString) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            // JSON이 아닌 경우 원본 반환
            return jsonString;
        }
    }
}