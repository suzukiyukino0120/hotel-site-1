package com.example.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Reservation {
	private Integer reservationId;
	private Integer reservationUser;
	private String name;
	private String nameKana;
	private String telephoneNumber;
	private Date checkinDate;
	private Date checkoutDate;
	private String checkinTime;
	private Integer guestNumber;
	private Plan plan;
	private String status;
	private Integer totalPrice;
	
	/**
	 * 宿泊合計料金を計算する
	 * @param planFee　プラン料金
	 * @param roomFee　部屋料金
	 * @param stayDays　宿泊日数
	 * @param guestNumber　宿泊人数
	 * @return　宿泊合計料金
	 */
	public static Integer calcTotalPrice(Integer planFee, Integer[] roomFee, Integer stayDays, Integer guestNumber) {
		Integer totalPrice = planFee*stayDays*guestNumber;
		for(Integer fee:roomFee) {
			totalPrice += fee;
		}
		return totalPrice;
	}
}
