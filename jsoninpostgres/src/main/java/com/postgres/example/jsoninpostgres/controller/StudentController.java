package com.postgres.example.jsoninpostgres.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgres.example.jsoninpostgres.domain.StudentRequest;
import com.postgres.example.jsoninpostgres.entity.Address;
import com.postgres.example.jsoninpostgres.entity.Student;
import com.postgres.example.jsoninpostgres.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@RestController
@Slf4j
public class StudentController {
    private RestTemplate restTemplate = new RestTemplate();
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student")
    private Student addStudent(@RequestBody StudentRequest studentReq) throws JsonProcessingException {
        log.info("Student to be added is {}", studentReq);

        Student student = getStudent(studentReq);
        ObjectMapper objectMapper = new ObjectMapper();
        student.setFavs(objectMapper.writeValueAsString(studentReq.getFavs()));
        return studentService.addStudent(student);
    }

    @GetMapping("/student")
    private List<StudentRequest> getAllStudent() throws Exception {
        return studentService.getAllStudents();
    }

    //Name is one of the columns in our table. So we can use it to query.
    @GetMapping("/student/filterByName/{name}")
    private List<StudentRequest> getStudentsByName(@PathVariable String name) throws JsonProcessingException {
        return studentService.getStudentsByName(name);
    }

    //Our table has a Primary Key of City, State. So we need to pass both city and state to get the student by Id.
    @GetMapping("/student/filterById")
    private StudentRequest getStudentsById(@RequestParam String city, @RequestParam String state) throws JsonProcessingException {
        return studentService.getStudentsById(city, state);
    }

    //favs is a jsonb column in our table. We may want to filter favs by a specific key and value.
    @GetMapping("/student/filterByJsonColumnKeyValue/{key}/{value}")
    private List<StudentRequest> getStudentsByFavs(@PathVariable String key, @PathVariable String value) throws JsonProcessingException {
        return studentService.getStudentsByFavs(key, value);
    }

    //update a specific key in json column favs to a new String type value. If key doesnt exist, create it.
    @PatchMapping("/student/updateFavs/{key}/{value}/{id}")
    private void updateStudentFavsKeyValueOrCreateIfNotExists(@PathVariable String key, @PathVariable String value, @PathVariable int id) {
        studentService.updateStudentFavsKeyValueOrCreateIfNotExists(key, value, id);
    }

    //update a specific key in json column favs to a new Int type value. If key doesnt exist, create it.
    @PatchMapping("/student/updateFavsIntType/{key}/{value}/{id}")
    private void updateStudentFavsKeyValueOrCreateIfNotExistsIntType(@PathVariable String key, @PathVariable int value, @PathVariable int id) {
        studentService.updateStudentFavsKeyValueOrCreateIfNotExistsIntType(key, value, id);
    }

    //Update the json column favs entry entirely. This will replace the existing favs column entry with the new one for specified ID.
    @PatchMapping("/student")
    private void updateStudent(@RequestBody StudentRequest studentReq) throws JsonProcessingException {
        Student student = getStudent(studentReq);
        ObjectMapper objectMapper = new ObjectMapper();
        student.setFavs(objectMapper.writeValueAsString(studentReq.getFavs()));
        studentService.updateStudentFavs(student.getFavs(), student.getId());
    }

    //Delete a student record by name.
    @DeleteMapping("/student/{name}")
    private int deleteStudent(@PathVariable String name) {
        return studentService.deleteStudent(name);
    }

    //Our table has json column. We can remove specific key-value pair from the json column.
    @DeleteMapping("/student/keyValue/{key}/{id}")
    private void deleteStudentFavsKeyValue(@PathVariable String key, @PathVariable int id) {
        studentService.deleteStudentFavsKeyValue(key, id);
    }

    private static Student getStudent(StudentRequest studentReq) {
        Student student = new Student();
        student.setName(studentReq.getName());
        student.setAge(studentReq.getAge());
        Address address = new Address();
        address.setCity(studentReq.getCity());
        address.setState(studentReq.getState());

        student.setAddress(address);

        student.setId(studentReq.getId());
        student.setAdult(studentReq.isAdult());
        return student;
    }

}