package com.example.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import com.example.validator.DateIntegrety;
import com.example.validator.VacancyRoomExists;

@Data
@VacancyRoomExists(planId="planId", startDate="checkinDate", endDate="checkoutDate")
@DateIntegrety(startDate="checkinDate", endDate="checkoutDate")
public class ConfirmPlanForm {
	private Integer planId;
	private String planName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkinDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="チェックアウト日を入力してください")
	private LocalDate checkoutDate;
	
	@NotNull(message="宿泊人数を入力してください")
	private Integer guestNumber;

}
