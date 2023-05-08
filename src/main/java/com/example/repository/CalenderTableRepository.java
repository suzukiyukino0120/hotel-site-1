package com.example.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Calender;

@Repository
public class CalenderTableRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Calender> CALENDER_ROW_MAPPER
	= (rs,i) -> {
		Calender calender = new Calender();
		calender.setDate(rs.getDate("date"));
		calender.setRoomId(rs.getInt("room_id"));
		calender.setVacancyRooms(rs.getInt("vacancy_rooms"));
		calender.setAccomodationFee(rs.getInt("accomondation_fee"));
		return calender;
	};
	
	/**
	 * 各日付の空室状況・料金を取得するSELECT文
	 * @param startDate
	 * @param endDate
	 * @param planId
	 * @return　日付・部屋ID・空室数・宿泊料金
	 */
	public List<Calender> selectByPlanId(LocalDate startDate, LocalDate endDate, Integer planId){
		String sql = "SELECT c.date, c.room_id, c.vacancy_rooms, "
				+ "(c.room_fee + p.plan_fee) as accomondation_fee "
				+ "FROM PLAN_MST as p "
				+ "JOIN (SELECT date, room_id, vacancy_rooms, room_fee "
						+ "FROM CALENDER_TABLE WHERE date BETWEEN :startDate AND :endDate  AND del_flg='0') as c "
				+ "ON p.room_id = c.room_id "
				+"WHERE p.plan_id = :planId  AND p.del_flg='0' ORDER BY c.date";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("startDate", startDate)
				.addValue("endDate", endDate)
				.addValue("planId", planId);
		return template.query(sql,param,CALENDER_ROW_MAPPER);
	}
	
	/**
	 * 空室状況を更新する
	 * @param roomId
	 * @param checkinDate
	 * @param checkoutDate
	 */
	public void updateVacancyRoom(Integer roomId, LocalDate checkinDate, LocalDate checkoutDate) {
		String sql ="UPDATE CALENDER_TABLE SET vacancy_rooms = vacancy_rooms-1 WHERE room_id = :roomId AND date BETWEEN :checkinDate AND (:checkoutDate +cast('-1 days' as INTERVAL))";
		SqlParameterSource param = new MapSqlParameterSource().addValue("roomId", roomId).addValue("checkinDate", checkinDate).addValue("checkoutDate", checkoutDate);
		template.update(sql, param);
	}
}
