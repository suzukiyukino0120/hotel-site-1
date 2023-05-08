package com.example.createForm;

import java.util.List;
import java.util.Map;

import com.example.domain.Code;
import com.example.domain.Plan;
import com.example.domain.Room;

import lombok.Data;

@Data
public class ReservationSearchContents {
	private List<Code> status;
	private List<Plan> planId;
	private List<Room> roomId;
	private List<Code> mealType;
	private List<Code> roomType;
	private List<Code> roomRank;
	private List<Code> smokingType;
	private List<Code> bathroomType;

}
