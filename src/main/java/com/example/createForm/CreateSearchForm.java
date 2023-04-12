package com.example.createForm;

import java.util.Map;

import lombok.Data;

@Data
public class CreateSearchForm {
	private Map<String,String> mealType;
	private Map<String,String> roomType;
	private Map<String,String> roomRank;
	private Map<String,String> smokingType;
	private Map<String,String> bathroomType;
	private Map<Integer,Integer> guestCapacity;	
}
