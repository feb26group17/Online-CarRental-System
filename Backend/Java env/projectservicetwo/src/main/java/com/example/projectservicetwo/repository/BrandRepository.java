package com.example.projectservicetwo.repository;

import com.example.projectservicetwo.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    boolean existsByBnameIgnoreCase(String bname);

    boolean existsByBnameIgnoreCaseAndBrandIdNot(String bname, Integer brandId);
}