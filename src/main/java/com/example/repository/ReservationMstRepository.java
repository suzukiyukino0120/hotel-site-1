package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Plan;
import com.example.domain.Reservation;
import com.example.domain.Room;
import com.example.form.ReservationForm;
import com.example.form.SearchReservationForm;

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
		reservation.setStatus(rs.getString("status"));
		Plan plan = new Plan();
		plan.setPlanId(rs.getInt("plan_id"));
		plan.setPlanName(rs.getString("plan_name"));
		reservation.setPlan(plan);
		return reservation;
	};
	
	public static final RowMapper<Reservation> RESERVATION_DETAIL_ROW_MAPPER=
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
				reservation.setStatus(rs.getString("status"));
				Room room =new Room();
				room.setRoomId(rs.getInt("room_id"));
				room.setRoomType(rs.getString("room_type"));
				room.setRoomRank(rs.getString("room_rank"));
				room.setSmokingType(rs.getString("smoking_type"));
				room.setBathroomType(rs.getString("bathroom_type"));
				Plan plan = new Plan();
				plan.setPlanId(rs.getInt("plan_id"));
				plan.setPlanName(rs.getString("plan_name"));
				plan.setMealType(rs.getString("meal_type"));
				plan.setImagePhoto(rs.getString("image_photo"));
				plan.setPlanContents(rs.getString("plan_contents"));
				plan.setRoom(room);
				reservation.setPlan(plan);
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
		String sql="INSERT INTO RESERVATION_TABLE (reservation_user, name, name_kana, checkin_date, checkout_date, checkin_time, guest_number, telephone_number, total_price, plan_id, status, create_user, create_date, update_user, update_date, del_flg) "+
		"VALUES(:reservationUser, :name, :nameKana, :checkinDate, :checkoutDate, :checkinTime, :guestNumber, :telephoneNumber, :totalPrice, :planId, '0', :createUser, CURRENT_DATE, :updateUser, CURRENT_DATE, '0');";
		SqlParameterSource param = new BeanPropertySqlParameterSource(reservationForm);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"reservation_id"};
		template.update(sql, param, keyHolder, keyColumnNames);
		return keyHolder.getKey().intValue();//予約番号
	}
	
	/**
	 * 予約番号で検索
	 * @param reservationId
	 * @return
	 */
	public List<Reservation> selectById(Integer reservationId) {
		String sql = "SELECT "
				+"r.reservation_id,"
				+ " r.reservation_user,"
				+ " r.name,"
				+ " r.name_kana,"
				+ " r.telephone_number,"
				+ " r.checkin_date,"
				+ " r.checkout_date,"
				+ " r.checkin_time,"
				+ " r.guest_number,"
				+ " r.total_price,"
				+ " r.plan_id, "
				+" r.status, "
				+ "p.plan_name "
				+"FROM RESERVATION_TABLE as r "
				+"JOIN PLAN_MST as p ON r.plan_id = p.plan_id "
				+"JOIN ROOM_MST as rm ON p.room_id = rm.room_id "
				+"WHERE r.reservation_id = :reservationId AND r.del_flg='0'";
		SqlParameterSource param = new MapSqlParameterSource().addValue("reservationId",reservationId);
		return template.query(sql, param, RESERVATION_ROW_MAPPER);
	}
	
	/**
	 * 予約者名で検索
	 * @param reservationId
	 * @return
	 */
	public List<Reservation> selectByReservationUser(Integer reservationUser) {
		String sql = "SELECT "
				+ "r.reservation_id, "
				+ "r.reservation_user, "
				+ "r.name, "
				+ "r.name_kana, "
				+ "r.telephone_number, "
				+ "r.checkin_date, "
				+ "r.checkout_date, "
				+ "r.checkin_time, "
				+ "r.guest_number, "
				+ "r.total_price, "
				+ "r.plan_id, "
				+" r.status,"
				+ " p.plan_name"
				+ " FROM RESERVATION_TABLE as r"
				+ " JOIN PLAN_MST as p ON r.plan_id = p.plan_id"
				+ " WHERE r.reservation_user = :reservationUser AND r.del_flg='0' ORDER BY r.checkin_date";
		SqlParameterSource param = new MapSqlParameterSource().addValue("reservationUser",reservationUser);
		return template.query(sql, param, RESERVATION_ROW_MAPPER);
	}
	
	/**
	 * 入力された条件で検索
	 * @param reservationId
	 * @return
	 */
	public List<Reservation> selectByArbitrary(SearchReservationForm searchReservationForm) {
		String sql = "SELECT "
				+"r.reservation_id, "
				+ "r.reservation_user, "
				+ "r.name, "
				+ "r.name_kana, "
				+ "r.telephone_number, "
				+ "r.checkin_date, "
				+ "r.checkout_date, "
				+ "r.checkin_time, "
				+ "r.guest_number, "
				+ "r.total_price, "
				+ "r.plan_id, "
				+" r.status, "
				+ "p.plan_name "
				+"FROM RESERVATION_TABLE as r "
				+"JOIN PLAN_MST as p ON r.plan_id = p.plan_id "
				+"JOIN ROOM_MST as rm ON p.room_id = rm.room_id WHERE r.del_flg='0'";
		if(searchReservationForm.getReservationId()!=null) {
			sql+=" AND r.reservation_id=:reservationId";
		}
		if(searchReservationForm.getPlanId()!=null) {
			sql+=" AND p.plan_id=:planId";
		}
		if(searchReservationForm.getRoomId()!=null) {
			sql+=" AND rm.room_id=:roomId";
		}
		if(!searchReservationForm.getMealType().isBlank()) {
			sql+=" AND p.meal_type=:mealType";
		}
		if(!searchReservationForm.getRoomType().isBlank()) {
			sql+=" AND rm.room_type=:roomType";
		}
		if(!searchReservationForm.getRoomRank().isBlank()) {
			sql+=" AND rm.room_rank=:roomRank";
		}
		if(!searchReservationForm.getSmokingType().isBlank()) {
			sql+=" AND rm.smoking_type=:smokingType";
		}
		if(!searchReservationForm.getBathroomType().isBlank()) {
			sql+=" AND rm.bathroom_type=:bathroomType";
		}
		if(searchReservationForm.getCheckinDate()!=null) {
			sql+=" AND r.checkin_date=:checkinDate";
		}
		if(searchReservationForm.getCheckoutDate()!=null) {
			sql+=" AND r.checkout_date=:checkoutDate";
		}
		if(!searchReservationForm.getStatus().isBlank()) {
			sql+=" AND r.status=:status";
		}
		sql+=" ORDER BY r.checkin_date";
		SqlParameterSource param = new BeanPropertySqlParameterSource(searchReservationForm);
		return template.query(sql, param, RESERVATION_ROW_MAPPER);
	}
	
	/**
	 * 予約詳細情報（プラン・部屋・予約情報）の取得
	 * @param reservationId
	 * @return
	 */
	public Reservation selectDetailInfoByReservationId(Integer reservationId) {
		String sql="SELECT "
				+ "r.reservation_id, "
				+ "r.reservation_user, "
				+ "r.name, "
				+ "r.name_kana, "
				+ "r.telephone_number, "
				+ "r.checkin_date, "
				+ "r.checkout_date, "
				+ "r.checkin_time, "
				+ "r.guest_number, "
				+ "r.total_price, "
				+ "r.plan_id, "
				+" r.status, "
				+ "p.plan_name, "
				+"p.meal_type, "
				+ "p.image_photo, "
				+ "p.plan_contents, "
				+ "rm.room_id, "
				+"rm.room_type, "
				+"rm.room_rank, "
				+"rm.smoking_type, "
				+"rm.bathroom_type "
				+ "FROM RESERVATION_TABLE as r "
				+ "JOIN PLAN_MST as p ON r.plan_id=p.plan_id "
				+ "JOIN ROOM_MST as rm ON p.room_id=rm.room_id "
				+ "WHERE r.reservation_id=:reservationId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("reservationId",reservationId);
		return template.queryForObject(sql, param, RESERVATION_DETAIL_ROW_MAPPER);
	}
	
	/**
	 * ステータスの更新
	 * @param reservationId
	 * @param status
	 */
	public void updateStatus(Integer reservationId, String status) {
		String sql="UPDATE RESERVATION_TABLE SET status = :status WHERE reservation_id = :reservationId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("reservationId",reservationId).addValue("status",status);
		template.update(sql, param);
	}
}
