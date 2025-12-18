package com.example.hotel;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.hotel.model.RoomType;
import com.example.hotel.service.HotelService;

@SpringBootApplication
public class Test2HotelReservationSystemApplication implements CommandLineRunner {
	@Autowired
	private HotelService service;

	public static void main(String[] args) {
		SpringApplication.run(Test2HotelReservationSystemApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	
		service.setRoom(1, RoomType.STANDARD, 1000);
		service.setRoom(2, RoomType.JUNIOR, 2000);
		service.setRoom(3, RoomType.SUITE, 3000);
		
		service.setUser(1, 5000);
		service.setUser(2, 10000);

		// User 1 tries booking Room 2 from 30/06/2026 to 07/07/2026 (7 nights)
		// -> insufficient balance
		try {
			service.bookRoom(1, 2, fmt.parse("30/06/2026"), fmt.parse("07/07/2026"));
		} catch (Exception e) {
			System.out.println("Booking attempt 1 failed: " + e.getMessage());
		}
	
		// User 1 tries booking Room 2 from 07/07/2026 to 30/06/2026
		// -> invalid range
		try {
			service.bookRoom(1, 2, fmt.parse("07/07/2026"), fmt.parse("30/06/2026"));
		} catch (Exception e) {
			System.out.println("Booking attempt 2 failed: " + e.getMessage());
		}

		// User 1 tries booking Room 1 from 07/07/2026 to 08/07/2026 (1 night)
		// -> success, user 1 now has 4000
		try {
			service.bookRoom(1, 1, fmt.parse("07/07/2026"), fmt.parse("08/07/2026"));
		} catch (Exception e) {
			System.out.println("Booking attempt 3 failed: " + e.getMessage());
		}
	
		// User 2 tries booking Room 1 from 07/07/2026 to 09/07/2026 (2 nights)
		// -> room unavailable
		try {
			service.bookRoom(2, 1, fmt.parse("07/07/2026"), fmt.parse("09/07/2026"));
		} catch (Exception e) {
			System.out.println("Booking attempt 4 failed: " + e.getMessage());
		}

		// User 2 tries booking Room 3 from 07/07/2026 to 08/07/2026 (1 night)
		// -> success, user 2 now has 7000
		try {
			service.bookRoom(2, 3, fmt.parse("07/07/2026"), fmt.parse("08/07/2026"));
		} catch (Exception e) {
			System.out.println("Booking attempt 5 failed: " + e.getMessage());
		}

		service.setRoom(1, RoomType.SUITE, 10000);

		System.out.println("\n--- print all rooms and bookings, latest to oldest ---");
		service.printAll();
	
		System.out.println("\n--- print all users, latest to oldest ---");
		service.printAllUsers();
	}
}
