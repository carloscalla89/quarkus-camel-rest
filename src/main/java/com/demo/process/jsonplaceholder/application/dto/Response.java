package com.demo.process.jsonplaceholder.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@RegisterForReflection // <-- clave para nativo
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Response {

    public int userId;
    public int id;
    public String title;
    public String body;
}
