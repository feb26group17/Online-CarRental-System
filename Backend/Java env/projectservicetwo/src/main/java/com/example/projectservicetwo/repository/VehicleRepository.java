package com.example.projectservicetwo.repository;

import com.example.projectservicetwo.entity.Vehicle;
import com.example.projectservicetwo.entity.Vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findByOwnerId(Integer ownerId);

    List<Vehicle> findByStatus(VehicleStatus status);
}