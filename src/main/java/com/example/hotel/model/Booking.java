package com.example.hotel.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class Booking {
	private static final AtomicLong COUNTER = new AtomicLong(1);
	private final long bookingId;

	// snapshot of the room when booking was created
	private final int roomNumberSnapshot;
	private final RoomType roomTypeSnapshot;
	private final int roomPricePerNightSnapshot;

	// snapshot of the user when booking was created
	private final int userIdSnapshot;
	private final int userBalanceBefore;
	private final int userBalanceAfter;

	private final LocalDate checkIn;
	private final LocalDate checkOut;
	private final int totalPrice;

	public Booking(
	  int roomNumberSnapshot,
	  RoomType roomTypeSnapshot,
	  int roomPricePerNightSnapshot,
	  int userIdSnapshot,
	  int userBalanceBefore,
	  int userBalanceAfter,	
	  LocalDate checkIn,
	  LocalDate checkOut,
	  int totalPrice
	) {
		this.bookingId = COUNTER.getAndIncrement();
		this.roomNumberSnapshot = roomNumberSnapshot;
		this.roomTypeSnapshot = roomTypeSnapshot;
		this.roomPricePerNightSnapshot = roomPricePerNightSnapshot;
		this.userIdSnapshot = userIdSnapshot;
		this.userBalanceBefore = userBalanceBefore;
		this.userBalanceAfter = userBalanceAfter;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.totalPrice = totalPrice;
	}

	public long getBookingId() {
		return bookingId;
	}

	public int getRoomNumberSnapshot() {
		return roomNumberSnapshot;
	}

	public RoomType getRoomTypeSnapshot() {
		return roomTypeSnapshot;
	}

	public int getRoomPricePerNightSnapshot() {
		return roomPricePerNightSnapshot;
	}
	
	public int getUserIdSnapshot() {
		return userIdSnapshot;
	}
	
	public int getUserBalanceBefore() {
		return userBalanceBefore;
	}
	
	public int getUserBalanceAfter() {
		return userBalanceAfter;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}
	
	public LocalDate getCheckOut() {
		return checkOut;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		return "Booking { " + "bookingId: " + bookingId 
							+ ", roomNumber: " + roomNumberSnapshot
							+ ", roomType: " + roomTypeSnapshot
							+ ", roomPricePerNight: " + roomPricePerNightSnapshot
							
							+ ", userId: " + userIdSnapshot
							+ ", userBalanceBefore: " + userBalanceBefore
							+ "\n, userBalanceAfter: " + userBalanceAfter
							
							+ ", checkIn: " + checkIn.format(formatter)
							+ ", checkOut: " + checkOut.format(formatter)
							+ ", totalPrice: " + totalPrice
							+ " }";
	}
}
