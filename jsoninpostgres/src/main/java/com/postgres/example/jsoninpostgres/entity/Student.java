package com.postgres.example.jsoninpostgres.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
//@TypeDefs({
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
public class Student implements Serializable {

    private int id;
    private String name;
    private int age;

    //Below will map the city property of the Address class to city column in the Student database table. Its not needed, if names are same.
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride( name = "city", column = @Column(name = "city"))})
    private Address address;
    private boolean adult;
    //@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String favs;
}