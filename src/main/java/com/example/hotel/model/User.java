package com.example.hotel.model;

public class User {
	private int userId;
	private int balance;

	public User(int userId, int balance) {
		this.userId = userId;
		this.balance = balance;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "User { " + "userId: " + userId + ", balance: " + balance + " }";
	}
}
