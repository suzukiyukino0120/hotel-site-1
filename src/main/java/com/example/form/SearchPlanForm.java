package com.example.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SearchPlanForm {
	private String[] mealType;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	private String[] roomType;
	private String[] roomRank;
	private String[] smokingType;
	private String[] bathroomType;
	private Integer guestCapacity;
}
