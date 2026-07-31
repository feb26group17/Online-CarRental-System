package com.example.projectservicetwo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.qos.logback.core.model.Model;


@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {


   

}
