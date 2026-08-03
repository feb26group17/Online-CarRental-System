package com.carrental.crudservice.repository;

import com.carrental.crudservice.entity.Vehicle;
import com.carrental.crudservice.entity.enums.FuelType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByUserId(Integer userId);

    @Query("SELECT v FROM Vehicle v JOIN Model m ON v.modelId = m.modelId " +
           "WHERE (:brandId IS NULL OR m.brandId = :brandId) " +
           "AND (:modelId IS NULL OR v.modelId = :modelId) " +
           "AND (:fuelType IS NULL OR v.fuelType = :fuelType) " +
           "AND (:minPrice IS NULL OR v.rentPerDay >= :minPrice) " +
           "AND (:maxPrice IS NULL OR v.rentPerDay <= :maxPrice)")
    List<Vehicle> searchVehicles(@Param("brandId") Integer brandId,
                                 @Param("modelId") Integer modelId,
                                 @Param("fuelType") FuelType fuelType,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);
}
