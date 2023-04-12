package com.example.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Calender {
	private Date date;
	private String dateType;
	private Integer roomId;
	private Integer totalRooms;
	private Integer vacancyRooms;
	private Integer roomFee;
}
