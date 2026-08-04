package com.carrental.bookingservice.service;

import com.carrental.bookingservice.client.CrudServiceClient;
import com.carrental.bookingservice.dto.BookingRequest;
import com.carrental.bookingservice.dto.BookingResponse;
import com.carrental.bookingservice.entity.*;
import com.carrental.bookingservice.entity.enums.BookingStatus;
import com.carrental.bookingservice.entity.enums.VehicleStatus;
import com.carrental.bookingservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CrudServiceClient crudServiceClient;
    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    public BookingService(BookingRepository bookingRepository,
                          CrudServiceClient crudServiceClient,
                          ModelRepository modelRepository,
                          BrandRepository brandRepository) {
        this.bookingRepository = bookingRepository;
        this.crudServiceClient = crudServiceClient;
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
    }

    @Transactional
    public BookingResponse createBooking(Integer userId, BookingRequest request) {
        // RestTemplate call to ocrs-crud-service on port 8082
        Customer customer = crudServiceClient.getCustomerByUserId(userId);
        Vehicle vehicle = crudServiceClient.getVehicleById(request.getVehicleId());

        if (vehicle.getStatus() != VehicleStatus.Available) {
            throw new RuntimeException("Vehicle is currently not available for rent");
        }

        if (request.getReturnDate().isBefore(request.getPickupDate())) {
            throw new RuntimeException("Return date cannot be before pickup date");
        }

        boolean hasOverlap = bookingRepository
                .existsByVehicleIdAndStatusNotAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
                        request.getVehicleId(),
                        BookingStatus.Cancelled,
                        request.getReturnDate(),
                        request.getPickupDate()
                );

        if (hasOverlap) {
            throw new RuntimeException("Vehicle is already booked for the selected dates");
        }

        long days = ChronoUnit.DAYS.between(request.getPickupDate(), request.getReturnDate());
        if (days <= 0) days = 1;
        BigDecimal totalAmount = vehicle.getRentPerDay().multiply(BigDecimal.valueOf(days));

        Booking booking = Booking.builder()
                .customerId(customer.getCustomerId())
                .vehicleId(request.getVehicleId())
                .pickupDate(request.getPickupDate())
                .returnDate(request.getReturnDate())
                .dropCity(request.getDropCity())
                .totalDays((int) days)
                .totalAmount(totalAmount)
                .status(BookingStatus.Pending)
                .build();

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return mapToResponses(bookingRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Integer userId) {
        Customer customer = crudServiceClient.getCustomerByUserId(userId);
        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerId());
        return mapToResponses(bookings);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Integer id, Integer userId, String role) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        if (!"ADMIN".equalsIgnoreCase(role)) {
            if ("CUSTOMER".equalsIgnoreCase(role)) {
                Customer customer = crudServiceClient.getCustomerByUserId(userId);
                if (!booking.getCustomerId().equals(customer.getCustomerId())) {
                    throw new RuntimeException("Unauthorized: You do not own this booking");
                }
            } else if ("OWNER".equalsIgnoreCase(role)) {
                Vehicle vehicle = crudServiceClient.getVehicleById(booking.getVehicleId());
                if (!vehicle.getUserId().equals(userId)) {
                    throw new RuntimeException("Unauthorized: You do not own the vehicle for this booking");
                }
            } else {
                throw new RuntimeException("Unauthorized access to booking");
            }
        }

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(Integer id, Integer userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        Customer customer = crudServiceClient.getCustomerByUserId(userId);

        if (!booking.getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("Unauthorized: You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.Completed || booking.getStatus() == BookingStatus.Cancelled) {
            throw new RuntimeException("Booking cannot be cancelled in status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.Cancelled);

        // Update vehicle status via RestTemplate to crud-service
        crudServiceClient.updateVehicleStatus(booking.getVehicleId(), "Available");

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    @Transactional
    public BookingResponse updateBookingStatus(Integer id, String statusStr) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid booking status: " + statusStr);
        }

        booking.setStatus(newStatus);

        if (newStatus == BookingStatus.Confirmed) {
            crudServiceClient.updateVehicleStatus(booking.getVehicleId(), "Booked");
        } else if (newStatus == BookingStatus.Completed || newStatus == BookingStatus.Cancelled) {
            crudServiceClient.updateVehicleStatus(booking.getVehicleId(), "Available");
        }

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsForOwnerVehicle(Integer vehicleId, Integer userId) {
        Vehicle vehicle = crudServiceClient.getVehicleById(vehicleId);

        if (!vehicle.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You do not own this vehicle");
        }

        List<Booking> bookings = bookingRepository.findByVehicleId(vehicleId);
        return mapToResponses(bookings);
    }

    private List<BookingResponse> mapToResponses(List<Booking> bookings) {
        Map<Integer, Model> modelMap = modelRepository.findAll().stream()
                .collect(Collectors.toMap(Model::getModelId, m -> m));
        Map<Integer, Brand> brandMap = brandRepository.findAll().stream()
                .collect(Collectors.toMap(Brand::getBrandId, b -> b));

        return bookings.stream().map(b -> {
            Vehicle vehicle = null;
            try {
                vehicle = crudServiceClient.getVehicleById(b.getVehicleId());
            } catch (Exception e) {
                // fallback if vehicle fetch fails
            }

            String regNum = vehicle != null ? vehicle.getRegistrationNumber() : "Unknown";
            Model model = vehicle != null ? modelMap.get(vehicle.getModelId()) : null;
            String modelName = model != null ? model.getModelName() : "Unknown";
            Brand brand = model != null ? brandMap.get(model.getBrandId()) : null;
            String brandName = brand != null ? brand.getBname() : "Unknown";

            BigDecimal rent = vehicle != null ? vehicle.getRentPerDay() : BigDecimal.ZERO;
            long days = b.getTotalDays() != null ? b.getTotalDays() : ChronoUnit.DAYS.between(b.getPickupDate(), b.getReturnDate());
            if (days <= 0) days = 1;

            BigDecimal totalAmount = b.getTotalAmount() != null ? b.getTotalAmount() : rent.multiply(BigDecimal.valueOf(days));

            return BookingResponse.builder()
                    .bookingId(b.getBookingId())
                    .customerId(b.getCustomerId())
                    .vehicleId(b.getVehicleId())
                    .vehicleRegistrationNumber(regNum)
                    .brandName(brandName)
                    .modelName(modelName)
                    .bookingDate(b.getBookingDate())
                    .pickupDate(b.getPickupDate())
                    .returnDate(b.getReturnDate())
                    .dropCity(b.getDropCity())
                    .status(b.getStatus())
                    .totalAmount(totalAmount)
                    .rentPerDay(rent)
                    .build();
        }).collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return mapToResponses(List.of(booking)).get(0);
    }
}
