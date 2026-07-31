package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.UserResponseDTO;
import com.example.projectservicetwo.dto.VehicleResponseDTO;
import com.example.projectservicetwo.entity.User;
import com.example.projectservicetwo.entity.Vehicle;
import com.example.projectservicetwo.entity.Vehicle.VehicleStatus;
import com.example.projectservicetwo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Get all vehicles
    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get vehicle by ID
    @Transactional(readOnly = true)
    public VehicleResponseDTO getVehicleById(Integer id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        return mapToDTO(vehicle);
    }

    // Approve vehicle (sets status to Available)
    @Transactional
    public VehicleResponseDTO approveVehicle(Integer id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        
        vehicle.setStatus(VehicleStatus.Available);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToDTO(updatedVehicle);
    }

    // Reject vehicle (sets status to Maintenance or inactive state)
    @Transactional
    public VehicleResponseDTO rejectVehicle(Integer id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        vehicle.setStatus(VehicleStatus.Maintenance);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToDTO(updatedVehicle);
    }

    // Delete vehicle permanently
    @Transactional
    public void deleteVehicle(Integer id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    // Helper method to map Entity to DTO
    private VehicleResponseDTO mapToDTO(Vehicle vehicle) {
        UserResponseDTO ownerDTO = null;
        if (vehicle.getOwner() != null) {
            User owner = vehicle.getOwner();
            ownerDTO = new UserResponseDTO(
                    owner.getId(),
                    owner.getName(),
                    owner.getEmail(),
                    owner.getPhone(),
                    owner.getRole(),
                    owner.getAddress(),
                    owner.getStatus(),
                    owner.getAdharCard(),
                    owner.getCreatedAt()
            );
        }

        Integer modelId = (vehicle.getModel() != null) ? vehicle.getModel().getModelId() : null;

        return new VehicleResponseDTO(
                vehicle.getVehicleId(),
                ownerDTO,
                modelId,
                vehicle.getRegistrationNumber(),
                vehicle.getFuelType(),
                vehicle.getRentPerDay(),
                vehicle.getStatus()
        );
    }
}