package com.example.projectservicetwo.controller;

import com.example.projectservicetwo.dto.ModelRequestDTO;
import com.example.projectservicetwo.dto.ModelResponseDTO;
import com.example.projectservicetwo.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
public class ModelController {

    private final ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public ResponseEntity<List<ModelResponseDTO>> getAllModels() {
        return ResponseEntity.ok(modelService.getAllModels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResponseDTO> getModelById(@PathVariable("id") Integer modelId) {
        ModelResponseDTO model = modelService.getModelById(modelId);
        if (model != null) {
            return ResponseEntity.ok(model);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ModelResponseDTO>> getModelsByBrandId(@PathVariable("brandId") Integer brandId) {
        return ResponseEntity.ok(modelService.getModelsByBrandId(brandId));
    }

    @PostMapping
    public ResponseEntity<ModelResponseDTO> createModel(@RequestBody ModelRequestDTO modelRequestDTO) {
        ModelResponseDTO createdModel = modelService.createModel(modelRequestDTO);
        if (createdModel != null) {
            return new ResponseEntity<>(createdModel, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelResponseDTO> updateModel(
            @PathVariable("id") Integer modelId,
            @RequestBody ModelRequestDTO modelRequestDTO) {
        ModelResponseDTO updatedModel = modelService.updateModel(modelId, modelRequestDTO);
        if (updatedModel != null) {
            return ResponseEntity.ok(updatedModel);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable("id") Integer modelId) {
        modelService.deleteModel(modelId);
        return ResponseEntity.ok("Model deleted successfully with ID: " + modelId);
    }
}