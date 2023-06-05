package com.postgres.example.jsoninpostgres.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgres.example.jsoninpostgres.domain.StudentRequest;
import com.postgres.example.jsoninpostgres.entity.Address;
import com.postgres.example.jsoninpostgres.entity.Student;
import com.postgres.example.jsoninpostgres.repository.StudentJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    StudentServiceImpl( StudentJpaRepository studentJpaRepository){
        this.studentJpaRepository = studentJpaRepository;
    }

    @Override
    public Student addStudent(Student student) {
        return studentJpaRepository.save(student);
    }

    @Override
    public List<StudentRequest> getAllStudents() throws JsonProcessingException {
         List<Student> studentList = studentJpaRepository.findAll();
        return getStudentRequestList(studentList);
    }

    @Override
    public List<StudentRequest> getStudentsByName(String name) throws JsonProcessingException {
        List<Student> studentList = studentJpaRepository.findByName(name);
        return getStudentRequestList(studentList);
    }

    @Override
    public StudentRequest getStudentsById(String city, String state) throws JsonProcessingException {
        Optional<Student> studentOptional = studentJpaRepository.findById(new Address(city, state));
        if(!studentOptional.isPresent()){
            return null;
        }
        Student s = studentOptional.get();
        return getStudentRequestList(Collections.singletonList(s)).get(0);
    }

    @Override
    public List<StudentRequest> getStudentsByFavs(String key, String value) throws JsonProcessingException {
        List<Student> studentList = studentJpaRepository.findByFavs(key, value);
        if(studentList == null || studentList.isEmpty()){
            return null;
        }
        List<StudentRequest> studentRequestList = getStudentRequestList(studentList);
        return studentRequestList;
    }

    @Transactional
    @Override
    public void updateStudentFavsKeyValueOrCreateIfNotExists(String key, String value, int id) {
        log.info("Update request received by StudentServiceImpl for updating: {} to {} for ID={}", key, value, id);
        studentJpaRepository.updateStudentFavsKeyValueOrCreateIfNotExists(key, value, id);
    }

    @Transactional
    @Override
    public void updateStudentFavsKeyValueOrCreateIfNotExistsIntType(String key, int value, int id) {
        log.info("Update request received by StudentServiceImpl for updating: {} to {} for ID={}", key, value, id);
        studentJpaRepository.updateStudentFavsKeyValueOrCreateIfNotExistsIntType(key, value, id);
    }

    @Transactional
    @Override
    public void updateStudentFavs(String favs, int id) {
        studentJpaRepository.updateStudentFavs(favs, id);
    }

    @Transactional
    @Override
    public int deleteStudent(String name) {
        return studentJpaRepository.deleteByName(name);
    }

    @Transactional
    @Override
    public void deleteStudentFavsKeyValue(String keyName, int id) {
         studentJpaRepository.deleteFavsKeyValuePair(keyName, id);
    }

    private List<StudentRequest> getStudentRequestList(List<Student> studentList) throws JsonProcessingException {
        List<StudentRequest> studentRequestList = new ArrayList<>();
        for (Student student: studentList) {
            ObjectMapper objectMapper = new ObjectMapper();
                StudentRequest studentRequest = new StudentRequest();
                studentRequest.setId(student.getId());
                studentRequest.setName(student.getName());
                studentRequest.setAge(student.getAge());
                studentRequest.setCity(student.getAddress().getCity());
                studentRequest.setState(student.getAddress().getState());
                studentRequest.setAdult(student.isAdult());
                studentRequest.setFavs(objectMapper.readValue(student.getFavs(), HashMap.class));
                studentRequestList.add(studentRequest);
        }
        return studentRequestList;
    }
}