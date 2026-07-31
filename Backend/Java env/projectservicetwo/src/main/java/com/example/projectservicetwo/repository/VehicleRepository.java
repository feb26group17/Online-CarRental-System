package com.example.projectservicetwo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.Vehicle;
import com.example.projectservicetwo.entity.Vehicle.VehicleStatus;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {


    
}