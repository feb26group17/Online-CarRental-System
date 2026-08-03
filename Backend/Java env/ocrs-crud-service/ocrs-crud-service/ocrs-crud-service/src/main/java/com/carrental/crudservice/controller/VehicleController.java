package com.carrental.crudservice.controller;

import com.carrental.crudservice.dto.StatusUpdateRequest;
import com.carrental.crudservice.dto.VehicleRequest;
import com.carrental.crudservice.dto.VehicleResponse;
import com.carrental.crudservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> browseVehicles(
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer modelId,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(vehicleService.searchVehicles(brandId, modelId, fuelType, minPrice, maxPrice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Integer id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request,
                                                          Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        VehicleResponse response = vehicleService.createVehicle(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<VehicleResponse>> getMyVehicles(Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(vehicleService.getMyVehicles(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable Integer id,
                                                          @Valid @RequestBody VehicleRequest request,
                                                          Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(vehicleService.updateVehicle(id, userId, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<VehicleResponse> updateVehicleStatus(@PathVariable Integer id,
                                                                @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicleStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id,
                                               Authentication authentication) {
        Integer userId = getUserIdFromAuth(authentication);
        vehicleService.deleteVehicle(id, userId);
        return ResponseEntity.noContent().build();
    }

    @SuppressWarnings("unchecked")
    private Integer getUserIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            Object userIdObj = details.get("userId");
            if (userIdObj instanceof Integer) {
                return (Integer) userIdObj;
            }
        }
        throw new RuntimeException("User ID not found in authentication token");
    }
}
