package com.example.projectservicetwo.repository;

import com.example.projectservicetwo.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {

    // Helper query to find all models belonging to a specific brand
    List<Model> findByBrandBrandId(Integer brandId);
}