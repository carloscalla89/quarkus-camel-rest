package com.demo.process.jsonplaceholder.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonPlaceHolderDtoResponse {

    private String id;
    private String title;
    private String body;
    private String userId;
}
