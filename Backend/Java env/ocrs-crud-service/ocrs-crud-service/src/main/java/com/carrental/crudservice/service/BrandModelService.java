package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.BrandResponse;
import com.carrental.crudservice.dto.ModelResponse;
import com.carrental.crudservice.entity.Brand;
import com.carrental.crudservice.entity.Model;
import com.carrental.crudservice.repository.BrandRepository;
import com.carrental.crudservice.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrandModelService {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

    public BrandModelService(BrandRepository brandRepository, ModelRepository modelRepository) {
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(b -> BrandResponse.builder()
                        .brandId(b.getBrandId())
                        .bname(b.getBname())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ModelResponse> getModelsByBrand(Integer brandId) {
        Map<Integer, String> brandMap = brandRepository.findAll().stream()
                .collect(Collectors.toMap(Brand::getBrandId, Brand::getBname));

        List<Model> models = (brandId != null) ?
                modelRepository.findByBrandId(brandId) :
                modelRepository.findAll();

        return models.stream()
                .map(m -> ModelResponse.builder()
                        .modelId(m.getModelId())
                        .brandId(m.getBrandId())
                        .brandName(brandMap.getOrDefault(m.getBrandId(), "Unknown"))
                        .modelName(m.getModelName())
                        .seatingCapacity(m.getSeatingCapacity())
                        .build())
                .collect(Collectors.toList());
    }
}
