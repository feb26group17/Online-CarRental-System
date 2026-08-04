package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.BookingRequest;
import com.carrental.crudservice.dto.BookingResponse;
import com.carrental.crudservice.entity.Booking;
import com.carrental.crudservice.entity.Customer;
import com.carrental.crudservice.entity.Model;
import com.carrental.crudservice.entity.Vehicle;
import com.carrental.crudservice.entity.enums.BookingStatus;
import com.carrental.crudservice.repository.BookingRepository;
import com.carrental.crudservice.repository.CustomerRepository;
import com.carrental.crudservice.repository.ModelRepository;
import com.carrental.crudservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelRepository modelRepository;

    public BookingService(BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          VehicleRepository vehicleRepository,
                          ModelRepository modelRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.modelRepository = modelRepository;
    }

    public BookingResponse createBooking(Integer userId, BookingRequest request) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + request.getVehicleId()));

        Booking booking = Booking.builder()
                .customerId(customer.getCustomerId())
                .vehicleId(request.getVehicleId())
                .pickupDate(request.getPickupDate())
                .returnDate(request.getReturnDate())
                .dropCity(request.getDropCity())
                .status(BookingStatus.Pending)
                .build();

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public List<BookingResponse> getMyBookings(Integer userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerId());
        return mapToResponses(bookings);
    }

    public BookingResponse getBookingById(Integer id, Integer userId, String role) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        if (!"ADMIN".equalsIgnoreCase(role)) {
            if ("CUSTOMER".equalsIgnoreCase(role)) {
                Customer customer = customerRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));
                if (!booking.getCustomerId().equals(customer.getCustomerId())) {
                    throw new RuntimeException("Unauthorized: you do not have access to this booking");
                }
            } else if ("OWNER".equalsIgnoreCase(role)) {
                Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId())
                        .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + booking.getVehicleId()));
                if (!vehicle.getUserId().equals(userId)) {
                    throw new RuntimeException("Unauthorized: you do not have access to this booking");
                }
            } else {
                throw new RuntimeException("Unauthorized: you do not have access to this booking");
            }
        }

        return mapToResponse(booking);
    }

    public BookingResponse cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setStatus(BookingStatus.Cancelled);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public List<BookingResponse> getBookingsByVehicle(Integer vehicleId, Integer userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));

        if (!vehicle.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: you do not own this vehicle");
        }

        List<Booking> bookings = bookingRepository.findByVehicleId(vehicleId);
        return mapToResponses(bookings);
    }

    public BookingResponse updateBookingStatus(Integer id, String statusStr) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        BookingStatus status = BookingStatus.valueOf(statusStr);
        booking.setStatus(status);

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public List<BookingResponse> getAllBookings() {
        return mapToResponses(bookingRepository.findAll());
    }

    private List<BookingResponse> mapToResponses(List<Booking> bookings) {
        Map<Integer, Vehicle> vehicleMap = vehicleRepository.findAll().stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, v -> v));
        Map<Integer, Model> modelMap = modelRepository.findAll().stream()
                .collect(Collectors.toMap(Model::getModelId, m -> m));

        return bookings.stream().map(b -> {
            Vehicle vehicle = vehicleMap.get(b.getVehicleId());
            String regNum = vehicle != null ? vehicle.getRegistrationNumber() : "Unknown";
            Model model = vehicle != null ? modelMap.get(vehicle.getModelId()) : null;
            String modelName = model != null ? model.getModelName() : "Unknown";

            long days = ChronoUnit.DAYS.between(b.getPickupDate(), b.getReturnDate());
            if (days <= 0) days = 1;
            BigDecimal rent = vehicle != null ? vehicle.getRentPerDay() : BigDecimal.ZERO;
            BigDecimal totalAmount = rent.multiply(BigDecimal.valueOf(days));

            return BookingResponse.builder()
                    .bookingId(b.getBookingId())
                    .customerId(b.getCustomerId())
                    .vehicleId(b.getVehicleId())
                    .vehicleRegistrationNumber(regNum)
                    .modelName(modelName)
                    .bookingDate(b.getBookingDate())
                    .pickupDate(b.getPickupDate())
                    .returnDate(b.getReturnDate())
                    .dropCity(b.getDropCity())
                    .status(b.getStatus())
                    .totalAmount(totalAmount)
                    .build();
        }).collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return mapToResponses(List.of(booking)).get(0);
    }
}
