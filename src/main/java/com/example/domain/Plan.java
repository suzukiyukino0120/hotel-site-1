package com.example.domain;

import lombok.Data;

@Data
public class Plan {
	private Integer planId;
	private String planName;
	private String planContents;
	private String mealType;
	private Integer planFee;
	private Room room;
	private String ImagePhoto;
}
