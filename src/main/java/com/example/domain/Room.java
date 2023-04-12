package com.example.domain;

import java.util.List;

import lombok.Data;

@Data
public class Room {
	private Integer roomId;
	private String roomType;
	private String roomRank;
	private String smokingType;
	private String bathroomType;
	private Integer guestCapacity;
	private List<Calender> vacancyRoomCalender;
}
