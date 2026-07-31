package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.BookingResponseDTO;
import com.example.projectservicetwo.dto.CustomerResponseDTO;
import com.example.projectservicetwo.dto.UserResponseDTO;
import com.example.projectservicetwo.dto.VehicleResponseDTO;
import com.example.projectservicetwo.entity.Booking;
import com.example.projectservicetwo.entity.Booking.BookingStatus;
import com.example.projectservicetwo.entity.Customer;
import com.example.projectservicetwo.entity.User;
import com.example.projectservicetwo.entity.Vehicle;
import com.example.projectservicetwo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Get all bookings
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get booking by ID
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return mapToDTO(booking);
    }

    // Cancel booking
    @Transactional
    public BookingResponseDTO cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setStatus(BookingStatus.Cancelled);

        // Optional: If vehicle status is tied to active bookings, update vehicle status back to Available
        if (booking.getVehicle() != null) {
            booking.getVehicle().setStatus(Vehicle.VehicleStatus.Available);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDTO(updatedBooking);
    }

    // Delete booking permanently
    @Transactional
    public void deleteBooking(Integer id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    // Helper method to map Entity to DTO
    private BookingResponseDTO mapToDTO(Booking booking) {
        CustomerResponseDTO customerDTO = null;
        if (booking.getCustomer() != null) {
            Customer customer = booking.getCustomer();
            UserResponseDTO userDTO = null;

            if (customer.getUser() != null) {
                User u = customer.getUser();
                userDTO = new UserResponseDTO(
                        u.getId(), u.getName(), u.getEmail(), u.getPhone(),
                        u.getRole(), u.getAddress(), u.getStatus(),
                        u.getAdharCard(), u.getCreatedAt()
                );
            }

            customerDTO = new CustomerResponseDTO(
                    customer.getCustomerId(),
                    customer.getDrivingLicense(),
                    userDTO
            );
        }

        VehicleResponseDTO vehicleDTO = null;
        if (booking.getVehicle() != null) {
            Vehicle v = booking.getVehicle();
            UserResponseDTO ownerDTO = null;

            if (v.getOwner() != null) {
                User o = v.getOwner();
                ownerDTO = new UserResponseDTO(
                        o.getId(), o.getName(), o.getEmail(), o.getPhone(),
                        o.getRole(), o.getAddress(), o.getStatus(),
                        o.getAdharCard(), o.getCreatedAt()
                );
            }

            Integer modelId = (v.getModel() != null) ? v.getModel().getModelId() : null;

            vehicleDTO = new VehicleResponseDTO(
                    v.getVehicleId(),
                    ownerDTO,
                    modelId,
                    v.getRegistrationNumber(),
                    v.getFuelType(),
                    v.getRentPerDay(),
                    v.getStatus()
            );
        }

        return new BookingResponseDTO(
                booking.getBookingId(),
                customerDTO,
                vehicleDTO,
                booking.getBookingDate(),
                booking.getPickupDate(),
                booking.getReturnDate(),
                booking.getStatus()
        );
    }
}