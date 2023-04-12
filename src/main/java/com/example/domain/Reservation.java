package com.example.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Reservation {
	private Integer reservationId;
	private Integer reservationUser;
	private String name;
	private String nameKana;
	private String zipCode;
	private String address;
	private String telephoneNumber;
	private Date checkinDate;
	private Date checkoutDate;
	private Integer guestNumber;
	private Integer planId;
	private String cancelStatus;
	private Integer totalPrice;
}
