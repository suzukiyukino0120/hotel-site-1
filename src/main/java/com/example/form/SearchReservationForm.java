package com.example.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SearchReservationForm {
	private Integer reservationId;
	private String status;
	private Integer planId;
	private Integer roomId;
	private String mealType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkinDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkoutDate;
	private String roomType;
	private String roomRank;
	private String smokingType;
	private String bathroomType;
}
