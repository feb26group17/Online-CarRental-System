package com.example.projectservicetwo.controller;

import com.example.projectservicetwo.dto.VehicleResponseDTO;
import com.example.projectservicetwo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // GET /vehicles
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // GET /vehicles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable Integer id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // PUT /vehicles/{id}/approve
    @PutMapping("/{id}/approve")
    public ResponseEntity<VehicleResponseDTO> approveVehicle(@PathVariable Integer id) {
        return ResponseEntity.ok(vehicleService.approveVehicle(id));
    }

    // PUT /vehicles/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<VehicleResponseDTO> rejectVehicle(@PathVariable Integer id) {
        return ResponseEntity.ok(vehicleService.rejectVehicle(id));
    }

    // DELETE /vehicles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle with ID " + id + " has been deleted successfully.");
    }
}