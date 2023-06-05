package com.postgres.example.jsoninpostgres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentRequest {

    private int id;
    private String name;
    private int age;
    private String city;
    private String state;
    private boolean adult;
    private Map<String, Object> favs;
}
