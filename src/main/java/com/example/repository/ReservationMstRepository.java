package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Reservation;
import com.example.form.ReservationForm;

@Repository
public class ReservationMstRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<Reservation> RESERVATION_ROW_MAPPER=
	(rs,i) -> {
		Reservation reservation = new Reservation();
		reservation.setReservationId(rs.getInt("reservation_id"));
		reservation.setReservationUser(rs.getInt("reservation_user"));
		reservation.setName(rs.getString("name"));
		reservation.setNameKana(rs.getString("name_kana"));
		reservation.setCheckinDate(rs.getDate("checkin_date"));
		reservation.setCheckoutDate(rs.getDate("checkout_date"));
		reservation.setCheckinTime(rs.getString("checkin_time"));
		reservation.setGuestNumber(rs.getInt("guest_number"));
		reservation.setTelephoneNumber(rs.getString("telephone_number"));
		reservation.setTotalPrice(rs.getInt("total_price"));
		reservation.setPlanId(rs.getInt("plan_id"));
		reservation.setCancelStatus(rs.getString("cancel_status"));
		return reservation;
	};
	
	/**
	 * 予約のレコード追加
	 * @param reservationForm
	 * @param createUser
	 * @param updateUser
	 * @return 予約番号
	 */
	public Integer insertReservation(ReservationForm reservationForm) {
		String sql="INSERT INTO RESERVATION_TABLE (reservation_user, name, name_kana, checkin_date, checkout_date, checkin_time, guest_number, telephone_number, total_price, plan_id, cancel_status, create_user, create_date, update_user, update_date, del_flg) "+
		"VALUES(:reservationUser, :name, :nameKana, :checkinDate, :checkoutDate, :checkinTime, :guestNumber, :telephoneNumber, :totalPrice, :planId, '0', :createUser, CURRENT_DATE, :updateUser, CURRENT_DATE, '0');";
		SqlParameterSource param = new BeanPropertySqlParameterSource(reservationForm);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"reservation_id"};
		template.update(sql, param, keyHolder, keyColumnNames);
		return keyHolder.getKey().intValue();//予約番号
	}
	
	public Reservation selectById(Integer reservationId) {
		String sql = "SELECT * FROM RESERVATION_TABLE WHERE reservation_id = :reservationId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("reservationId",reservationId);
		return template.queryForObject(sql, param, RESERVATION_ROW_MAPPER);
	}
}
