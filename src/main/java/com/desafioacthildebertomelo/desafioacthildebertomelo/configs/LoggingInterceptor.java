package com.desafioacthildebertomelo.desafioacthildebertomelo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// IMPORTAÇÃO CORRETA PARA O MÉTODO kv
import static net.logstash.logback.argument.StructuredArguments.kv;



@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("request",
                kv("method", request.getMethod()),
                kv("uri", request.getRequestURI()),
                kv("ip", request.getRemoteAddr()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 2. RECUPERA E FAZ O CASTING DE OBJECT PARA LONG
        Long startTime = (Long) request.getAttribute("startTime");
        long executionTime = (startTime != null) ? (System.currentTimeMillis() - startTime) : 0;
        logger.info("response",
                kv("status", response.getStatus()),
                kv("uri", request.getRequestURI()),
                kv("executionTime", executionTime));
    }
}

