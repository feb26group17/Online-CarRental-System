package com.carrental.bookingservice.client;

import com.carrental.bookingservice.entity.Customer;
import com.carrental.bookingservice.entity.Vehicle;
import com.carrental.bookingservice.entity.enums.VehicleStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class CrudServiceClient {

    private final RestTemplate restTemplate;
    private final String crudServiceUrl;

    public CrudServiceClient(RestTemplate restTemplate,
                             @Value("${crud.service.url:http://localhost:8082}") String crudServiceUrl) {
        this.restTemplate = restTemplate;
        this.crudServiceUrl = crudServiceUrl;
    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getRequest() != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            if (authHeader != null && !authHeader.isEmpty()) {
                headers.set("Authorization", authHeader);
            }
        }
        return headers;
    }

    public Customer getCustomerByUserId(Integer userId) {
        try {
            String url = crudServiceUrl + "/api/customers/user/" + userId;
            HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
            ResponseEntity<CustomerDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, CustomerDto.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                CustomerDto dto = response.getBody();
                Customer customer = new Customer();
                customer.setCustomerId(dto.getCustomerId());
                customer.setUserId(dto.getUserId());
                customer.setDrivingLicense(dto.getDrivingLicense());
                return customer;
            }
        } catch (Exception e) {
            System.err.println("Error fetching customer via RestTemplate: " + e.getMessage());
        }
        throw new RuntimeException("Customer profile not found for user ID: " + userId);
    }

    public Vehicle getVehicleById(Integer vehicleId) {
        try {
            String url = crudServiceUrl + "/api/vehicles/" + vehicleId;
            HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());
            ResponseEntity<VehicleDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, VehicleDto.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                VehicleDto dto = response.getBody();
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(dto.getVehicleId());
                vehicle.setUserId(dto.getUserId());
                vehicle.setModelId(dto.getModelId());
                vehicle.setRegistrationNumber(dto.getRegistrationNumber());
                vehicle.setRentPerDay(dto.getRentPerDay());
                if (dto.getStatus() != null) {
                    try {
                        vehicle.setStatus(VehicleStatus.valueOf(dto.getStatus()));
                    } catch (Exception ex) {
                        vehicle.setStatus(VehicleStatus.Available);
                    }
                }
                return vehicle;
            }
        } catch (Exception e) {
            System.err.println("Error fetching vehicle via RestTemplate: " + e.getMessage());
        }
        throw new RuntimeException("Vehicle not found with ID: " + vehicleId);
    }

    public void updateVehicleStatus(Integer vehicleId, String status) {
        try {
            String url = crudServiceUrl + "/api/vehicles/" + vehicleId + "/status";
            HttpHeaders headers = getAuthHeaders();
            headers.set("Content-Type", "application/json");
            Map<String, String> requestBody = Map.of("status", status);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Void.class);
        } catch (Exception e) {
            System.err.println("Error updating vehicle status via RestTemplate: " + e.getMessage());
        }
    }

    public static class CustomerDto {
        private Integer customerId;
        private Integer userId;
        private String drivingLicense;

        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public String getDrivingLicense() { return drivingLicense; }
        public void setDrivingLicense(String drivingLicense) { this.drivingLicense = drivingLicense; }
    }

    public static class VehicleDto {
        private Integer vehicleId;
        private Integer userId;
        private Integer modelId;
        private String registrationNumber;
        private BigDecimal rentPerDay;
        private String status;

        public Integer getVehicleId() { return vehicleId; }
        public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public Integer getModelId() { return modelId; }
        public void setModelId(Integer modelId) { this.modelId = modelId; }
        public String getRegistrationNumber() { return registrationNumber; }
        public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
        public BigDecimal getRentPerDay() { return rentPerDay; }
        public void setRentPerDay(BigDecimal rentPerDay) { this.rentPerDay = rentPerDay; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
