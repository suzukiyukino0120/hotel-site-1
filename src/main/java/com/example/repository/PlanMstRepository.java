package com.example.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Calender;
import com.example.domain.Plan;
import com.example.domain.Room;
import com.example.form.SearchPlanForm;

@Repository
public class PlanMstRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;	
	
	private final static ResultSetExtractor<List<Plan>> PLAN_RESULT_SET_EXTRACTOR
	=(rs) -> {
		Map<Integer,Plan> planMap = new LinkedHashMap<Integer,Plan>();
		Map<Integer,Room> roomMap = new LinkedHashMap<Integer,Room>();
		Map<Map<Date,Integer>,Calender> calenderMap = new LinkedHashMap<Map<Date,Integer>,Calender>();
		Plan plan = null;		
		Room room = null;		
		Calender calender = null;
		while(rs.next()) {
			Integer planId = rs.getInt("plan_id");
			plan = planMap.get(planId);
			if(plan == null) {
				plan = new Plan();
				plan.setPlanId(rs.getInt("plan_id"));
				plan.setPlanName(rs.getString("plan_name"));
				plan.setPlanContents(rs.getString("plan_contents"));
				plan.setMealType(rs.getString("meal_type"));
				plan.setPlanFee(rs.getInt("plan_fee"));
				plan.setImagePhoto(rs.getString("image_photo"));
				planMap.put(planId, plan);
			}
		
			Integer roomId = rs.getInt("room_id");
			room = roomMap.get(roomId);
			if(room == null) {
				room = new Room();
				room.setRoomId(rs.getInt("room_id"));
				room.setRoomType(rs.getString("room_type"));
				room.setRoomRank(rs.getString("room_rank"));
				room.setBathroomType(rs.getString("bathRoom_type"));
				room.setSmokingType(rs.getString("smoking_type"));
				room.setGuestCapacity(rs.getInt("guest_capacity"));
				room.setVacancyRoomCalender(new ArrayList<Calender>());
				roomMap.put(roomId, room);
			}
			
			Date date = rs.getDate("date");
			Map<Date,Integer> keyMap = new LinkedHashMap<Date,Integer>();
			keyMap.put(date, roomId);
			calender =  calenderMap.get(keyMap);
			if(calender==null) {
				calender = new Calender();
				calender.setDate(rs.getDate("date"));
				calender.setRoomId(rs.getInt("room_id"));
				calender.setRoomFee(rs.getInt("room_fee"));
				calender.setTotalRooms(rs.getInt("total_rooms"));
				calender.setVacancyRooms(rs.getInt("vacancy_rooms"));
				room.getVacancyRoomCalender().add(calender);
				calenderMap.put(keyMap,calender);
			}
			plan.setRoom(room);
		}
		if(planMap.size()==0) {
			return null;
		}
		return new ArrayList<Plan>(planMap.values());
	};
	
	private final static RowMapper<Plan> PLAN_ROOM_ROW_MAPPER=
			(rs,i) -> {
				Plan plan = new Plan();
				plan.setPlanId(rs.getInt("plan_id"));
				plan.setPlanName(rs.getString("plan_name"));
				plan.setPlanContents(rs.getString("plan_contents"));
				plan.setMealType(rs.getString("meal_type"));
				plan.setPlanFee(rs.getInt("plan_fee"));
				plan.setImagePhoto(rs.getString("image_photo"));
				
				Room room = new Room();
				room.setRoomId(rs.getInt("room_id"));
				room.setRoomType(rs.getString("room_type"));
				room.setRoomRank(rs.getString("room_rank"));
				room.setSmokingType(rs.getString("smoking_type"));
				room.setBathroomType(rs.getString("bathroom_type"));
				room.setGuestCapacity(rs.getInt("guest_capacity"));
				plan.setRoom(room);
				return plan;
			};
			
			private final static RowMapper<Plan> PLAN_ROW_MAPPER=
					(rs,i) -> {
						Plan plan = new Plan();
						plan.setPlanId(rs.getInt("plan_id"));
						plan.setPlanName(rs.getString("plan_name"));
						plan.setPlanContents(rs.getString("plan_contents"));
						plan.setMealType(rs.getString("meal_type"));
						plan.setPlanFee(rs.getInt("plan_fee"));
						plan.setImagePhoto(rs.getString("image_photo"));
						return plan;
					};
	
	/**
	 * 入力された検索条件でレコード取得
	 * @return　プラン一覧
	 */
	public List<Plan> search(SearchPlanForm form){
		String sql =
				"SELECT "+
					"p.plan_id, "+
					"p.plan_name, "+
					"p.plan_contents, "+
					"p.meal_type, "+
					"p.plan_fee, "+
					"p.image_photo, "+
					"r.room_id, "+
					"r.room_type, "+
					"r.bathroom_type, "+
					"r.smoking_type, "+
					"r.room_rank, "+
					"r.guest_capacity, "+
					"c.date, "+
					"c.total_rooms, "+
					"c.vacancy_rooms, "+
					"c.room_fee "+
				"FROM PLAN_MST as p "+
				"JOIN ROOM_MST as r ON p.room_id = r.room_id "+
				"JOIN CODE_MST as cd ON p.meal_type=cd.code OR r.room_type=cd.code OR r.bathroom_type=cd.code OR r.smoking_type=cd.code OR r.room_rank=cd.code "+
				"JOIN (SELECT date, "+
							"total_rooms, "+
							"vacancy_rooms, "+
							"room_fee, "+ 
							"room_id "+ 
						"FROM CALENDER_TABLE "+
						"WHERE date BETWEEN :startDate AND (:endDate +cast('-1 days' as INTERVAL)) AND "+
								"vacancy_rooms>0 AND del_flg='0') as c ON r.room_id = c.room_id "+
				"WHERE r.guest_capacity >= :guestCapacity AND "+
				"(select count(*) from CALENDER_TABLE "+
				"where date BETWEEN :startDate AND (:endDate +cast('-1 days' as INTERVAL)) AND "+
				"vacancy_rooms >0 and room_id= r.room_id AND del_flg='0') = cast(:endDate as date) - cast(:startDate as date) AND "+
				"p.del_flg='0' AND r.del_flg='0'";
				
				
				//入力された検索条件に応じてwhere句を生成
				//食事
				if(form.getMealType()!=null) {
					sql += "AND p.meal_type IN(";
					for(int i=0; i<form.getMealType().length-1; i++) {
						sql += "'" + form.getMealType()[i] + "',";
					}
					sql += "'" + form.getMealType()[form.getMealType().length-1] + "') ";
				}
				
				//和室・洋室
				if(form.getRoomType()!=null) {
					sql += "AND r.room_type IN(";
					for(int i=0; i<form.getRoomType().length-1; i++) {
						sql += "'" + form.getRoomType()[i] + "',";
					}
					sql += "'" + form.getRoomType()[form.getRoomType().length-1] + "') ";
				}
		
				//部屋グレード
				if(form.getRoomRank()!=null) {
					sql += "AND r.room_rank IN(";
					for(int i=0; i<form.getRoomRank().length-1; i++) {
						sql += "'" + form.getRoomRank()[i] + "',";
					}
					sql += "'" + form.getRoomRank()[form.getRoomRank().length-1] + "') ";
				}
				
				//客室露天風呂
				if(form.getBathroomType()!=null) {
					sql += "AND r.bathroom_type IN(";
					for(int i=0; i<form.getBathroomType().length-1; i++) {
						sql += "'" + form.getBathroomType()[i] + "',";
					}
					sql += "'" + form.getBathroomType()[form.getBathroomType().length-1] + "') ";
				}
				
				//禁煙・喫煙
				if(form.getSmokingType()!=null) {
					sql += "AND r.smoking_type IN(";
					for(int i=0; i<form.getSmokingType().length-1; i++) {
						sql += "'" + form.getSmokingType()[i] + "',";
					}
					sql += "'" + form.getSmokingType()[form.getSmokingType().length-1] + "')";
				}
		sql += ";";
				
				SqlParameterSource param = new BeanPropertySqlParameterSource(form);
				return template.query(sql, param, PLAN_RESULT_SET_EXTRACTOR);
	}
	
	/**
	 * プランIDで一件検索
	 * @param planId
	 * @return　plan
	 */
	public Plan selectPlanById(Integer planId) {
		String sql="SELECT "+
						"p.plan_id, "+
						"p.plan_name, "+
						"p.plan_contents, "+
						"p.meal_type, "+
						"p.plan_fee, "+
						"p.image_photo, "+
						"r.room_id, "+
						"r.room_type, "+
						"r.bathroom_type, "+
						"r.smoking_type, "+
						"r.room_rank, "+
						"r.guest_capacity "+
					"FROM PLAN_MST as p "+
					"JOIN ROOM_MST as r ON p.room_id = r.room_id "+
					"WHERE p.plan_id = :planId  AND p.del_flg='0' AND r.del_flg='0'";
		SqlParameterSource param = new MapSqlParameterSource().addValue("planId", planId);
		return template.queryForObject(sql,param,PLAN_ROOM_ROW_MAPPER);
	}
	
	/**
	 * プランID・日付を指定して空室があるプランを取得
	 * @param planId
	 * @param startDate
	 * @param endDate
	 * @return　plan
	 */
	public Plan selectVacancyRoomPlan(Integer planId, LocalDate startDate, LocalDate endDate) {
		String sql =
				"SELECT "+
					"p.plan_id, "+
					"p.plan_name, "+
					"p.plan_contents, "+
					"p.meal_type, "+
					"p.plan_fee, "+
					"p.image_photo, "+
					"r.room_id, "+
					"r.room_type, "+
					"r.bathroom_type, "+
					"r.smoking_type, "+
					"r.room_rank, "+
					"r.guest_capacity, "+
					"c.date, "+
					"c.total_rooms, "+
					"c.vacancy_rooms, "+
					"c.room_fee "+
				"FROM PLAN_MST as p "+
				"JOIN ROOM_MST as r ON p.room_id = r.room_id "+
				"JOIN (SELECT date, "+
							"total_rooms, "+
							"vacancy_rooms, "+
							"room_fee, "+ 
							"room_id "+ 
						"FROM CALENDER_TABLE "+
						"WHERE date BETWEEN :startDate AND (:endDate +cast('-1 days' as INTERVAL)) AND "+
								"vacancy_rooms>0 AND del_flg='0') as c ON r.room_id = c.room_id "+
				"WHERE p.plan_id= :planId AND "+
				"(select count(*) from CALENDER_TABLE "+
				"where date BETWEEN :startDate AND (:endDate +cast('-1 days' as INTERVAL)) AND "+
				"vacancy_rooms >0 and room_id= r.room_id AND del_flg='0') = cast(:endDate as date) - cast(:startDate as date) AND "+
				"p.del_flg='0' AND r.del_flg='0'";
				
		SqlParameterSource param = new MapSqlParameterSource().addValue("planId", planId).addValue("startDate", startDate).addValue("endDate", endDate);
		List<Plan> planList = template.query(sql, param, PLAN_RESULT_SET_EXTRACTOR);
		if(planList==null) {
			return null;
		}else {
			return planList.get(0);
		}
	}
	
	/**
	 * 全件検索
	 * @return
	 */
	public List<Plan> selectAll(){
		String sql="SELECT * FROM PLAN_MST WHERE del_flg='0'";
		return template.query(sql, PLAN_ROW_MAPPER);
	}

}
