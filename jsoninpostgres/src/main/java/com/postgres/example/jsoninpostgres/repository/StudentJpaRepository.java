package com.postgres.example.jsoninpostgres.repository;

import com.postgres.example.jsoninpostgres.entity.Address;
import com.postgres.example.jsoninpostgres.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Address> {

    List<Student> findByName(String name);

    @Query(value = "select * from student where favs->>?1 = ?2", nativeQuery = true)
    List<Student> findByFavs(String key, String value);


    //String query1 = "update student set favs= jsonb_set(favs , '{color}' , '\"darkgreen\"') where id=1";
    //We dont need single quotes in java in case we want to fill values dynamically using @Param.
    //jsonb_set takes column_name, key, value and boolean as arguments. Boolean signifies whether to create a new field if it already doesnt exist.
    String query1 = "update student set favs= jsonb_set( favs , CAST(CONCAT('{', CAST(:key AS text), '}') as text[]), to_jsonb(CAST(:value AS text)), true) where id=:id";
    @Modifying
    @Query(value = query1, nativeQuery = true)
    void updateStudentFavsKeyValueOrCreateIfNotExists(@Param("key") String key, @Param("value") String value, @Param("id") int id);


    //String query2 = "update student set favs= jsonb_set(favs , '{attempts}' , '25') where id=1";
    String query2 = "update student set favs= jsonb_set( favs , CAST(CONCAT('{', CAST(:key AS text), '}') as text[]), to_jsonb(CAST(:value AS int)), true) where id=:id";
    @Modifying
    @Query(value = query2, nativeQuery = true)
    void updateStudentFavsKeyValueOrCreateIfNotExistsIntType(String key, int value, int id);

    @Modifying
    @Query(value = "update student set favs= ?1 where id=?2", nativeQuery = true)
    void updateStudentFavs(String favs, int id);

    int deleteByName(String name);

    @Modifying
    @Query(value = "UPDATE student SET favs = favs - CAST(?1 as text) WHERE id = CAST(?2 as int);", nativeQuery = true)
    void deleteFavsKeyValuePair(String keyName, int id);
}

