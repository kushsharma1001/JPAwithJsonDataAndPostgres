package com.postgres.example.jsoninpostgres.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.postgres.example.jsoninpostgres.domain.StudentRequest;
import com.postgres.example.jsoninpostgres.entity.Student;

import java.util.List;

public interface StudentService {

    Student addStudent(Student student) throws JsonProcessingException;

    List<StudentRequest> getAllStudents() throws JsonProcessingException;

    List<StudentRequest> getStudentsByName(String name) throws JsonProcessingException;

    StudentRequest getStudentsById(String city, String state) throws JsonProcessingException;

    List<StudentRequest> getStudentsByFavs(String key, String value) throws JsonProcessingException;

    void updateStudentFavsKeyValueOrCreateIfNotExists(String key, String value, int id);

    void updateStudentFavsKeyValueOrCreateIfNotExistsIntType(String key, int value, int id);

    void updateStudentFavs(String favs, int id);

    int deleteStudent(String name);

    void deleteStudentFavsKeyValue(String keyName, int id);
}