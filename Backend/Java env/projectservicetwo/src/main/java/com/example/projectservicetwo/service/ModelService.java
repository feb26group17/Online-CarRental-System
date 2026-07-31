package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.ModelRequestDTO;
import com.example.projectservicetwo.dto.ModelResponseDTO;
import com.example.projectservicetwo.entity.Brand;
import com.example.projectservicetwo.entity.Model;
import com.example.projectservicetwo.repository.BrandRepository;
import com.example.projectservicetwo.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository, BrandRepository brandRepository) {
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
    }

    public List<ModelResponseDTO> getAllModels() {
        return modelRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ModelResponseDTO getModelById(Integer modelId) {
        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (modelOptional.isPresent()) {
            return mapToDTO(modelOptional.get());
        }
        return null;
    }

    public List<ModelResponseDTO> getModelsByBrandId(Integer brandId) {
        return modelRepository.findByBrandBrandId(brandId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ModelResponseDTO createModel(ModelRequestDTO modelRequestDTO) {
        Optional<Brand> brandOptional = brandRepository.findById(modelRequestDTO.getBrandId());
        if (brandOptional.isPresent()) {
            Model model = new Model();
            model.setBrand(brandOptional.get());
            model.setModelName(modelRequestDTO.getModelName());
            model.setSeatingCapacity(modelRequestDTO.getSeatingCapacity());

            Model savedModel = modelRepository.save(model);
            return mapToDTO(savedModel);
        }
        return null;
    }

    public ModelResponseDTO updateModel(Integer modelId, ModelRequestDTO modelRequestDTO) {
        Optional<Model> modelOptional = modelRepository.findById(modelId);
        Optional<Brand> brandOptional = brandRepository.findById(modelRequestDTO.getBrandId());

        if (modelOptional.isPresent() && brandOptional.isPresent()) {
            Model existingModel = modelOptional.get();
            existingModel.setBrand(brandOptional.get());
            existingModel.setModelName(modelRequestDTO.getModelName());
            existingModel.setSeatingCapacity(modelRequestDTO.getSeatingCapacity());

            Model updatedModel = modelRepository.save(existingModel);
            return mapToDTO(updatedModel);
        }
        return null;
    }

    public void deleteModel(Integer modelId) {
        modelRepository.deleteById(modelId);
    }

    private ModelResponseDTO mapToDTO(Model model) {
        return new ModelResponseDTO(
                model.getModelId(),
                model.getBrand() != null ? model.getBrand().getBrandId() : null,
                model.getBrand() != null ? model.getBrand().getBname() : null,
                model.getModelName(),
                model.getSeatingCapacity()
        );
    }
}