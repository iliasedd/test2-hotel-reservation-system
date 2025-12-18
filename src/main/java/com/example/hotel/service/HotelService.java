package com.example.hotel.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.hotel.exception.InsufficientBalanceException;
import com.example.hotel.exception.InvalidInputException;
import com.example.hotel.exception.RoomNotFoundException;
import com.example.hotel.exception.RoomUnavailableException;
import com.example.hotel.exception.UserNotFoundException;
import com.example.hotel.model.Booking;
import com.example.hotel.model.Room;
import com.example.hotel.model.RoomType;
import com.example.hotel.model.User;

@Component
public class HotelService {
    private final ArrayList<Room> rooms = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Booking> bookings = new ArrayList<>();

    public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        if (roomNumber <= 0) throw new InvalidInputException("Invalid room number");
        if (roomPricePerNight <= 0) throw new InvalidInputException("Invalid room price");

        Optional<Room> existing = rooms.stream().filter(room -> room.getRoomNumber() == roomNumber).findFirst();
        
        if (existing.isPresent()) {
            Room room = existing.get();
            room.setType(roomType);
            room.setPricePerNight(roomPricePerNight);
            
            System.out.println("setRoom: updated room, " + room);
        } else {
            Room newRoom = new Room(roomNumber, roomType, roomPricePerNight);
            rooms.add(newRoom);
            
            System.out.println("setRoom: created new room, " + newRoom);
        }
    }

    public void setUser(int userId, int balance) {
        if (userId <= 0) throw new InvalidInputException("Invalid user id");
        if (balance < 0) throw new InvalidInputException("Invalid balance");

        Optional<User> existing = users.stream().filter(user -> user.getUserId() == userId).findFirst();
        
        if (existing.isPresent()) {
            User user = existing.get();
            user.setBalance(balance);
            
            System.out.println("setUser: updated user, " + user);
        } else {
            User newUser = new User(userId, balance);
            users.add(newUser);
            
            System.out.println("setUser: created new user, " + newUser);
        }
    }

    public void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) throw new InvalidInputException("CheckIn and checkOut dates must be provided");

        LocalDate in = toLocalDate(checkIn);
        LocalDate out = toLocalDate(checkOut);

        if (!in.isBefore(out)) {
            throw new InvalidInputException("CheckIn date must be before checkOut date");
        }

        long nights = ChronoUnit.DAYS.between(in, out);
        if (nights <= 0) throw new InvalidInputException("Booking period must be at least one night");

        User user = users.stream().filter(u -> u.getUserId() == userId).findFirst().orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Room room = rooms.stream().filter(r -> r.getRoomNumber() == roomNumber).findFirst().orElseThrow(() -> new RoomNotFoundException("Room with number " + roomNumber + " not found"));

        int totalPrice = (int) (nights * room.getPricePerNight());

        if (user.getBalance() < totalPrice) {
            throw new InsufficientBalanceException("User " + userId + " does not have enough balance. Required " + totalPrice + ", Available " + user.getBalance());
        }

        for (Booking b : bookings) {
            if (b.getRoomNumberSnapshot() != roomNumber) continue;

            if (in.isBefore(b.getCheckOut()) && out.isAfter(b.getCheckIn())) {
                throw new RoomUnavailableException("Room " + roomNumber + " is unavailable for the requested period (overlaps booking id " + b.getBookingId() + ")");
            }
        }

        int userBalanceBefore = user.getBalance();
        int userBalanceAfter = userBalanceBefore - totalPrice;
        user.setBalance(userBalanceAfter);

        // Create booking snapshot (contains room and user info at booking time)
        Booking booking = new Booking(
        	room.getRoomNumber(),
        	room.getType(),
        	room.getPricePerNight(),
            user.getUserId(),
            userBalanceBefore,
            userBalanceAfter,
            in,
            out,
            totalPrice
        );

        bookings.add(booking);
        System.out.println("bookRoom: Booking successful: " + booking);
    }

    private LocalDate toLocalDate(Date d) {
        Instant instant = Instant.ofEpochMilli(d.getTime());
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Print all rooms and all bookings from latest to oldest
    public void printAll() {
        for (int i = rooms.size() - 1; i >= 0; i--) {
            System.out.println(rooms.get(i));
        }

        System.out.print("\n");
        
        for (int i = bookings.size() - 1; i >= 0; i--) {
            System.out.println(bookings.get(i));
        }
    }

    // Print users from latest to oldest
    public void printAllUsers() {
        for (int i = users.size() - 1; i >= 0; i--) {
            System.out.println(users.get(i));
        }
    }
}
