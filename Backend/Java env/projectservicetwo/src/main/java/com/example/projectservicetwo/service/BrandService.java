package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.BrandRequestDTO;
import com.example.projectservicetwo.dto.BrandResponseDTO;
import com.example.projectservicetwo.entity.Brand;
import com.example.projectservicetwo.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandResponseDTO> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BrandResponseDTO getBrandById(Integer brandId) {
        Optional<Brand> brandOptional = brandRepository.findById(brandId);
        if (brandOptional.isPresent()) {
            return mapToDTO(brandOptional.get());
        }
        return null;
    }

    public BrandResponseDTO createBrand(BrandRequestDTO brandRequestDTO) {
        Brand brand = new Brand();
        brand.setBname(brandRequestDTO.getBname());

        Brand savedBrand = brandRepository.save(brand);
        return mapToDTO(savedBrand);
    }

    public BrandResponseDTO updateBrand(Integer brandId, BrandRequestDTO brandRequestDTO) {
        Optional<Brand> brandOptional = brandRepository.findById(brandId);
        if (brandOptional.isPresent()) {
            Brand existingBrand = brandOptional.get();
            existingBrand.setBname(brandRequestDTO.getBname());
            Brand updatedBrand = brandRepository.save(existingBrand);
            return mapToDTO(updatedBrand);
        }
        return null;
    }

    public void deleteBrand(Integer brandId) {
        brandRepository.deleteById(brandId);
    }

    private BrandResponseDTO mapToDTO(Brand brand) {
        return new BrandResponseDTO(brand.getBrandId(), brand.getBname());
    }
}