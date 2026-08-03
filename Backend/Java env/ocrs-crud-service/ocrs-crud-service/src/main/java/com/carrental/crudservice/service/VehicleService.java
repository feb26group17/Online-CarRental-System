package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.VehicleRequest;
import com.carrental.crudservice.dto.VehicleResponse;
import com.carrental.crudservice.entity.Brand;
import com.carrental.crudservice.entity.Model;
import com.carrental.crudservice.entity.Vehicle;
import com.carrental.crudservice.entity.enums.FuelType;
import com.carrental.crudservice.entity.enums.VehicleStatus;
import com.carrental.crudservice.repository.BrandRepository;
import com.carrental.crudservice.repository.ModelRepository;
import com.carrental.crudservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          ModelRepository modelRepository,
                          BrandRepository brandRepository) {
        this.vehicleRepository = vehicleRepository;
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
    }

    public List<VehicleResponse> searchVehicles(Integer brandId, Integer modelId, String fuelTypeStr, BigDecimal minPrice, BigDecimal maxPrice) {
        FuelType fuelType = null;
        if (fuelTypeStr != null && !fuelTypeStr.isBlank()) {
            try {
                fuelType = FuelType.valueOf(fuelTypeStr);
            } catch (Exception ignored) {}
        }

        List<Vehicle> vehicles = vehicleRepository.searchVehicles(brandId, modelId, fuelType, minPrice, maxPrice);
        return mapToResponses(vehicles);
    }

    public VehicleResponse getVehicleById(Integer id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        return mapToResponse(vehicle);
    }

    public VehicleResponse createVehicle(Integer userId, VehicleRequest request) {
        Vehicle vehicle = Vehicle.builder()
                .userId(userId)
                .modelId(request.getModelId())
                .registrationNumber(request.getRegistrationNumber())
                .fuelType(request.getFuelType())
                .rentPerDay(request.getRentPerDay())
                .status(request.getStatus() != null ? request.getStatus() : VehicleStatus.Available)
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToResponse(saved);
    }

    public List<VehicleResponse> getMyVehicles(Integer userId) {
        List<Vehicle> vehicles = vehicleRepository.findByUserId(userId);
        return mapToResponses(vehicles);
    }

    public VehicleResponse updateVehicle(Integer id, Integer userId, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        if (!vehicle.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You do not own this vehicle");
        }

        vehicle.setModelId(request.getModelId());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setRentPerDay(request.getRentPerDay());
        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToResponse(updated);
    }

    public VehicleResponse updateVehicleStatus(Integer id, String statusStr) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        VehicleStatus status = VehicleStatus.valueOf(statusStr);
        vehicle.setStatus(status);

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToResponse(updated);
    }

    public void deleteVehicle(Integer id, Integer userId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        if (!vehicle.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You do not own this vehicle");
        }

        vehicleRepository.delete(vehicle);
    }

    private List<VehicleResponse> mapToResponses(List<Vehicle> vehicles) {
        Map<Integer, Model> modelMap = modelRepository.findAll().stream()
                .collect(Collectors.toMap(Model::getModelId, m -> m));
        Map<Integer, String> brandMap = brandRepository.findAll().stream()
                .collect(Collectors.toMap(Brand::getBrandId, Brand::getBname));

        return vehicles.stream().map(v -> {
            Model model = modelMap.get(v.getModelId());
            String modelName = model != null ? model.getModelName() : "Unknown";
            String brandName = (model != null && brandMap.containsKey(model.getBrandId())) ? brandMap.get(model.getBrandId()) : "Unknown";
            Integer seating = model != null ? model.getSeatingCapacity() : null;

            return VehicleResponse.builder()
                    .vehicleId(v.getVehicleId())
                    .userId(v.getUserId())
                    .modelId(v.getModelId())
                    .modelName(modelName)
                    .brandName(brandName)
                    .seatingCapacity(seating)
                    .registrationNumber(v.getRegistrationNumber())
                    .fuelType(v.getFuelType())
                    .rentPerDay(v.getRentPerDay())
                    .status(v.getStatus())
                    .build();
        }).collect(Collectors.toList());
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return mapToResponses(List.of(vehicle)).get(0);
    }
}
