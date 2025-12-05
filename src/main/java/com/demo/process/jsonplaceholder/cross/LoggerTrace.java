package com.demo.process.jsonplaceholder.cross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.spi.Synchronization;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Named("logTrace")
@ApplicationScoped
public class LoggerTrace implements Processor, Synchronization {

    private ObjectMapper mapper;

    @Override
    public void process(Exchange exchange) throws Exception{

        exchange.getUnitOfWork().addSynchronization(this);
        String intercepted = exchange.getProperty(Exchange.TO_ENDPOINT, String.class);
        int countTrace = exchange.getProperty("count-trace", int.class) + 1;

        LocalDateTime localDateTime = LocalDateTime.now();
        exchange.setProperty("startTime", localDateTime);

        exchange.setProperty("count-trace", countTrace);

        MDC.put("endpoint-call", intercepted);
        MDC.put("count-trace", countTrace+"");
        MDC.put("type","REQUEST #"+countTrace);

        log.info("[PROCESS - trace-id:{}]",MDC.get("trace-id"));

    }

    @Override
    public void onComplete(Exchange exchange) {

        try {
            String bodyOut = exchange.getMessage().getBody(String.class);
            String responseCode = exchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, String.class);


            LocalDateTime startTime = exchange.getProperty("startTime", LocalDateTime.class);
            LocalDateTime endTime = LocalDateTime.now();

            long latence = ChronoUnit.MILLIS.between(startTime, endTime);
            int countTrace = exchange.getProperty("count-trace", int.class);

            MDC.put("latence",latence+"");
            MDC.put("type","RESPONSE #"+countTrace);
            MDC.put("status-response-call", responseCode);
            MDC.put("payload", bodyOut);

            log.info("[PROCESS COMPLETE - trace-id:{}]",MDC.get("trace-id"));

        } catch (Exception e) {
            log.error("Error onComplete:{}",e.getMessage());
        }

    }

    @Override
    public void onFailure(Exchange exchange) {

    }

    public void formatRequestRoot(Exchange exchange) throws Exception {
        setLogTraceToMDC(exchange);
    }

    public void formatResponseRoot(Exchange exchange) {

        try {

            LocalDateTime localDateTime = exchange.getProperty("startTimeRoot", LocalDateTime.class);

            String statusCode = Strings
                    .isNullOrEmpty(exchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, String.class)) ? "200"
                    : exchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, String.class);

            String bodyOut;
            Object body = exchange.getMessage().getBody();
            ObjectMapper mapper2 = new ObjectMapper();
            mapper2.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            if (body instanceof InputStreamCache inputStreamCache) {
                inputStreamCache.reset();
                bodyOut = new String(inputStreamCache.readAllBytes(), StandardCharsets.UTF_8);
            } else {
                bodyOut = mapper2.writeValueAsString(body);
            }

            LocalDateTime endTimeRoot = LocalDateTime.now();
            if (localDateTime == null) {
                localDateTime = LocalDateTime.now();
            }

            long latence = ChronoUnit.MILLIS.between(localDateTime, endTimeRoot);

            MDC.put("status", statusCode);
            MDC.put("latence",latence+"");
            MDC.put("type","RESPONSE END");
            MDC.put("payload", bodyOut);

            log.info("[END - trace-id:{}]",MDC.get("trace-id"));


        } catch (Exception e) {
            log.error("Error formatResponseRoot:{}", e.getMessage());
        }




    }

    private void setLogTraceToMDC(Exchange exchange) throws Exception {

        String rootPath = exchange.getContext().resolvePropertyPlaceholders("{{quarkus.http.root-path}}");
        String pathUri = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
        String endpointMethod = exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class);
        String traceId = exchange.getIn().getHeader("trace-id", String.class);
        Object body = exchange.getIn().getBody();

        MDC.put("type","REQUEST START");
        MDC.put("base-path", rootPath != null ? rootPath: "NOT_FOUND");
        MDC.put("endpoint", pathUri != null ? pathUri : "NOT_FOUND");
        MDC.put("method", endpointMethod != null ? endpointMethod : "NOT_FOUND");
        MDC.put("body", body != null ? entityToJson(body) : "NOT_FOUND");

        if(traceId == null || traceId.isBlank()) {
            traceId = "autogenerated";
            exchange.getMessage().setHeader("trace-id", traceId);
        }

        MDC.put("trace-id", traceId);
        log.info("[START - trace-id:{}]", traceId);

    }

    private String entityToJson(Object value) throws JsonProcessingException {
        if (value == null) {
            return "";
        }

        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        return mapper.writeValueAsString(value);
    }
}
