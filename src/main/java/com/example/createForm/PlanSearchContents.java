package com.example.createForm;

import java.util.List;
import java.util.Map;

import com.example.domain.Code;

import lombok.Data;

@Data
public class PlanSearchContents {
	private List<Code> mealType;
	private List<Code> roomType;
	private List<Code> roomRank;
	private List<Code> smokingType;
	private List<Code> bathroomType;
	private List<Code> guestCapacity;	
}
