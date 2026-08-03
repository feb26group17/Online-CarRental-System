package com.carrental.crudservice.controller;

import com.carrental.crudservice.dto.BrandResponse;
import com.carrental.crudservice.dto.ModelResponse;
import com.carrental.crudservice.service.BrandModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BrandModelController {

    private final BrandModelService brandModelService;

    public BrandModelController(BrandModelService brandModelService) {
        this.brandModelService = brandModelService;
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(brandModelService.getAllBrands());
    }

    @GetMapping("/models")
    public ResponseEntity<List<ModelResponse>> getModels(@RequestParam(required = false) Integer brandId) {
        return ResponseEntity.ok(brandModelService.getModelsByBrand(brandId));
    }
}
