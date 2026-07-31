package com.example.projectservicetwo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.Brand;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

  
}
