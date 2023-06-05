package com.postgres.example.jsoninpostgres.entity;

import lombok.*;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    private String city;
    private String state;
}