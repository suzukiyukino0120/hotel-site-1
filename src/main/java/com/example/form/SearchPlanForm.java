package com.example.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.validator.DateIntegrety;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@DateIntegrety(startDate="checkinDate", endDate="checkoutDate")
public class SearchPlanForm {
	private String[] mealType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	private String[] roomType;
	private String[] roomRank;
	private String[] smokingType;
	private String[] bathroomType;
	private Integer guestCapacity;
}
