package com.example.form;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.validator.VacancyRoomExists;

import lombok.Data;

@Data
@VacancyRoomExists(id="planId", startDate="checkinDate", endDate="checkoutDate")
public class ReservationForm {
	private Integer reservationUser;
	
	@NotBlank(message="氏名を入力してください")
	private String name;
	@NotBlank(message="ふりがなを入力してください")
	@Pattern(regexp="^[ぁ-んー]*$", message="ふりがなはひらがなで入力してください")
	private String nameKana;
	@NotBlank(message="連絡先を入力してください")
	@Pattern(regexp="^[0-9]{9,12}$", message="電話番号は半角数字で入力してください")
	private String telephoneNumber;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkinDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkoutDate;
	@NotBlank(message="チェックイン時刻を入力してください")
	private String checkinTime;
	private Integer guestNumber;
	private Integer planId;
	private Integer totalPrice;
	private Integer createUser;
	private Integer updateUser;

}
