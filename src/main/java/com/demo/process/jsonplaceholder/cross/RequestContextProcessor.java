package com.demo.process.jsonplaceholder.cross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class RequestContextProcessor implements Processor {

    private ObjectMapper mapper;

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        exchange.setProperty("request", body);
        exchange.setProperty("body", entityToJson(body));

        Object uuid = exchange.getIn().getHeader("trace-id", Object.class);
        if (Objects.isNull(uuid)) {
            uuid = "uuid-test";
        }

        LocalDateTime startTimeRoot = LocalDateTime.now();
        exchange.setProperty("startTimeRoot", startTimeRoot);
        exchange.setProperty("trace-id", uuid);
        exchange.setProperty("count-trace", 0);

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
