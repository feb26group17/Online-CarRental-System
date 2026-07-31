package com.example.projectservicetwo.controller;

import com.example.projectservicetwo.dto.BrandRequestDTO;
import com.example.projectservicetwo.dto.BrandResponseDTO;
import com.example.projectservicetwo.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> getBrandById(@PathVariable("id") Integer brandId) {
        BrandResponseDTO brand = brandService.getBrandById(brandId);
        if (brand != null) {
            return ResponseEntity.ok(brand);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BrandResponseDTO> createBrand(@RequestBody BrandRequestDTO brandRequestDTO) {
        BrandResponseDTO createdBrand = brandService.createBrand(brandRequestDTO);
        return new ResponseEntity<>(createdBrand, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> updateBrand(
            @PathVariable("id") Integer brandId,
            @RequestBody BrandRequestDTO brandRequestDTO) {
        BrandResponseDTO updatedBrand = brandService.updateBrand(brandId, brandRequestDTO);
        if (updatedBrand != null) {
            return ResponseEntity.ok(updatedBrand);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable("id") Integer brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok("Brand deleted successfully with ID: " + brandId);
    }
}